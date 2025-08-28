package com.salescore.salescore.core.ventas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salescore.salescore.core.ventas.dto.DetalleVentaDto;
import com.salescore.salescore.core.ventas.dto.VentaDto;
import com.salescore.salescore.core.ventas.mapper.VentaMapper;
import com.salescore.salescore.core.ventas.model.Venta;

import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VentaService {
    
    @Autowired
    private VentaMapper ventaMapper;
    
    @Autowired
    private ProductoService productoService;
    
    private List<Venta> ventas;
    private Long lastId;
    
    @PostConstruct
    public void init() {
        ventas = new ArrayList<>();
        lastId = 0L;
        
        // Datos iniciales serán creados en el controlador para demostrar el uso
    }
    
    public List<VentaDto> listarTodas() {
        return ventaMapper.modelsToDtos(ventas);
    }
    
    public Optional<VentaDto> buscarPorId(Long id) {
        return ventas.stream()
                .filter(venta -> venta.getId().equals(id))
                .findFirst()
                .map(venta -> ventaMapper.modelToDto(venta));
    }
    
    public List<VentaDto> buscarPorFechas(LocalDateTime inicio, LocalDateTime fin) {
        return ventas.stream()
                .filter(venta -> !venta.getFechaVenta().isBefore(inicio) && !venta.getFechaVenta().isAfter(fin))
                .map(venta -> ventaMapper.modelToDto(venta))
                .collect(Collectors.toList());
    }
    
    public VentaDto registrarVenta(VentaDto ventaDto) {
        lastId++;
        ventaDto.setId(lastId);
        ventaDto.setFechaVenta(LocalDateTime.now());
        
        // Asignar IDs a los detalles
        Long detalleId = 1L;
        for (DetalleVentaDto detalle : ventaDto.getDetalles()) {
            detalle.setId(detalleId++);
        }
        
        // Actualizar stock de productos
        for (DetalleVentaDto detalle : ventaDto.getDetalles()) {
            boolean stockActualizado = productoService.actualizarStock(
                    detalle.getProducto().getId(), 
                    detalle.getCantidad());
            
            if (!stockActualizado) {
                throw new RuntimeException("Stock insuficiente para el producto: " + detalle.getProducto().getNombre());
            }
        }
        
        // Calcular el total de la venta
        double total = ventaDto.getDetalles().stream()
                .mapToDouble(detalle -> detalle.getPrecioUnitario() * detalle.getCantidad())
                .sum();
        ventaDto.setTotal(total);
        
        // Guardar la venta
        Venta venta = ventaMapper.dtoToModel(ventaDto);
        ventas.add(venta);
        
        return ventaMapper.modelToDto(venta);
    }
    
    public double calcularGananciasPorPeriodo(LocalDateTime inicio, LocalDateTime fin) {
        return ventas.stream()
                .filter(venta -> !venta.getFechaVenta().isBefore(inicio) && !venta.getFechaVenta().isAfter(fin))
                .mapToDouble(Venta::getTotal)
                .sum();
    }
}
