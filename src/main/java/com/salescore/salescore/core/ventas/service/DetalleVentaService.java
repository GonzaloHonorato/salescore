package com.salescore.salescore.core.ventas.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.salescore.salescore.core.ventas.dto.DetalleVentaDto;
import com.salescore.salescore.core.ventas.mapper.DetalleVentaMapper;
import com.salescore.salescore.core.ventas.model.DetalleVenta;
import com.salescore.salescore.core.ventas.repository.DetalleVentaRepository;

@Service
@Transactional
public class DetalleVentaService {
    
    @Autowired
    private DetalleVentaRepository detalleVentaRepository;
    
    @Autowired
    private DetalleVentaMapper detalleVentaMapper;
    
    public List<DetalleVentaDto> listarTodos() {
        return detalleVentaRepository.findAll().stream()
                .map(detalleVentaMapper::toDto)
                .collect(Collectors.toList());
    }
    
    public Optional<DetalleVentaDto> buscarPorId(Integer id) {
        return detalleVentaRepository.findById(id)
                .map(detalleVentaMapper::toDto);
    }
    
    public List<DetalleVentaDto> buscarPorVenta(Integer ventaId) {
        return detalleVentaRepository.findByVentaId(ventaId).stream()
                .map(detalleVentaMapper::toDto)
                .collect(Collectors.toList());
    }
    
    public List<DetalleVentaDto> buscarPorProducto(Integer productoId) {
        return detalleVentaRepository.findByProductoId(productoId).stream()
                .map(detalleVentaMapper::toDto)
                .collect(Collectors.toList());
    }
    
    public DetalleVentaDto crear(DetalleVentaDto detalleVentaDto) {
        DetalleVenta detalleVenta = detalleVentaMapper.toEntity(detalleVentaDto);
        detalleVenta.setId(null); // Asegurar que es una nueva entidad
        DetalleVenta guardado = detalleVentaRepository.save(detalleVenta);
        return detalleVentaMapper.toDto(guardado);
    }
    
    public DetalleVentaDto actualizar(Integer id, DetalleVentaDto detalleVentaDto) {
        if (!detalleVentaRepository.existsById(id)) {
            throw new RuntimeException("DetalleVenta no encontrado con ID: " + id);
        }
        
        DetalleVenta detalleVenta = detalleVentaMapper.toEntity(detalleVentaDto);
        detalleVenta.setId(id);
        DetalleVenta actualizado = detalleVentaRepository.save(detalleVenta);
        return detalleVentaMapper.toDto(actualizado);
    }
    
    public void eliminar(Integer id) {
        if (!detalleVentaRepository.existsById(id)) {
            throw new RuntimeException("DetalleVenta no encontrado con ID: " + id);
        }
        detalleVentaRepository.deleteById(id);
    }
}
