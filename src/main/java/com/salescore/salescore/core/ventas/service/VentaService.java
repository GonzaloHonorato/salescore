package com.salescore.salescore.core.ventas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.salescore.salescore.core.ventas.dto.DetalleVentaDto;
import com.salescore.salescore.core.ventas.dto.VentaDto;
import com.salescore.salescore.core.ventas.mapper.VentaMapper;
import com.salescore.salescore.core.ventas.model.DetalleVenta;
import com.salescore.salescore.core.ventas.model.Venta;
import com.salescore.salescore.core.ventas.repository.DetalleVentaRepository;
import com.salescore.salescore.core.ventas.repository.VentaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class VentaService {
    
    @Autowired
    private VentaMapper ventaMapper;
    
    @Autowired
    private ProductoService productoService;
    
    @Autowired
    private VentaRepository ventaRepository;
    
    @Autowired
    private DetalleVentaRepository detalleVentaRepository;
    
    public List<VentaDto> listarTodas() {
        return ventaRepository.findAllWithDetalles().stream()
                .map(ventaMapper::toDto)
                .collect(Collectors.toList());
    }
    
    public Optional<VentaDto> buscarPorId(Integer id) {
        return ventaRepository.findByIdWithDetalles(id)
                .map(ventaMapper::toDto);
    }
    
    public List<VentaDto> buscarPorFechas(LocalDateTime inicio, LocalDateTime fin) {
        return ventaRepository.findByRangoFechas(inicio, fin).stream()
                .map(ventaMapper::toDto)
                .collect(Collectors.toList());
    }
    
    public VentaDto crear(VentaDto ventaDto) {
        // Procesar cada ítem para actualizar stock y validar existencias
        for (DetalleVentaDto detalle : ventaDto.getDetalles()) {
            boolean stockActualizado = productoService.actualizarStock(detalle.getProductoId(), detalle.getCantidad());
            if (!stockActualizado) {
                throw new RuntimeException("Stock insuficiente para el producto ID: " + detalle.getProductoId());
            }
        }
        
        // Convertir DTO a entidad
        Venta venta = ventaMapper.toEntity(ventaDto);
        venta.setId(null); // Asegurar que es nueva
        
        // Guardar la venta
        Venta guardada = ventaRepository.save(venta);
        
        // Calcular y devolver DTO con información completa
        return ventaMapper.toDto(guardada);
    }
    
    public VentaDto actualizar(Integer id, VentaDto ventaDto) {
        if (!ventaRepository.existsById(id)) {
            throw new RuntimeException("Venta no encontrada con ID: " + id);
        }
        
        // Restaurar stock de los ítems originales y actualizar con los nuevos
        Optional<Venta> ventaOriginalOpt = ventaRepository.findById(id);
        if (ventaOriginalOpt.isPresent()) {
            // Lógica para manejar el stock si fuera necesario
        }
        
        Venta venta = ventaMapper.toEntity(ventaDto);
        venta.setId(id);
        Venta actualizada = ventaRepository.save(venta);
        
        return ventaMapper.toDto(actualizada);
    }
    
    public void eliminar(Integer id) {
        if (!ventaRepository.existsById(id)) {
            throw new RuntimeException("Venta no encontrada con ID: " + id);
        }
        
        // Opcionalmente restaurar stock antes de eliminar
        
        ventaRepository.deleteById(id);
    }
    
    public double calcularGananciasPorPeriodo(LocalDateTime inicio, LocalDateTime fin) {
        List<Venta> ventasPeriodo = ventaRepository.findByRangoFechas(inicio, fin);
        return ventasPeriodo.stream()
                .mapToDouble(Venta::getTotal)
                .sum();
    }
    
    // Métodos de debug
    public Long contarVentasNativo() {
        return ventaRepository.countVentasNative();
    }
    
    public List<Object[]> obtenerVentasNativo() {
        return ventaRepository.findAllNative();
    }
}
