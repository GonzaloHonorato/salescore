package com.salescore.salescore.core.ventas.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salescore.salescore.core.ventas.dto.DetalleVentaDto;
import com.salescore.salescore.core.ventas.model.DetalleVenta;

@Component
public class DetalleVentaMapper {
    
    @Autowired
    private ProductoMapper productoMapper;
    
    // Métodos para compatibilidad con JPA
    public DetalleVentaDto toDto(DetalleVenta detalleVenta) {
        return modelToDto(detalleVenta);
    }
    
    public DetalleVenta toEntity(DetalleVentaDto detalleVentaDto) {
        return dtoToModel(detalleVentaDto);
    }
    
    public DetalleVentaDto modelToDto(DetalleVenta detalleVenta) {
        if (detalleVenta == null) {
            return null;
        }
        
        DetalleVentaDto dto = new DetalleVentaDto();
        dto.setId(detalleVenta.getId());
        dto.setProducto(productoMapper.toDto(detalleVenta.getProducto()));
        dto.setCantidad(detalleVenta.getCantidad());
        dto.setPrecioUnitario(detalleVenta.getPrecioUnitario());
        dto.setSubtotal(detalleVenta.getSubtotal());
        
        return dto;
    }
    
    public DetalleVenta dtoToModel(DetalleVentaDto detalleVentaDto) {
        if (detalleVentaDto == null) {
            return null;
        }
        
        DetalleVenta detalle = new DetalleVenta();
        detalle.setId(detalleVentaDto.getId());
        detalle.setProducto(productoMapper.toEntity(detalleVentaDto.getProducto()));
        detalle.setCantidad(detalleVentaDto.getCantidad());
        detalle.setPrecioUnitario(detalleVentaDto.getPrecioUnitario());
        detalle.setSubtotal(detalleVentaDto.getSubtotal());
        
        return detalle;
    }
    
    public List<DetalleVentaDto> modelsToDtos(List<DetalleVenta> detalleVentas) {
        if (detalleVentas == null) {
            return null;
        }
        
        List<DetalleVentaDto> detalleVentaDtos = new ArrayList<>();
        for (DetalleVenta detalleVenta : detalleVentas) {
            detalleVentaDtos.add(modelToDto(detalleVenta));
        }
        
        return detalleVentaDtos;
    }
    
    public List<DetalleVenta> dtosToModels(List<DetalleVentaDto> detalleVentaDtos) {
        if (detalleVentaDtos == null) {
            return null;
        }
        
        List<DetalleVenta> detalleVentas = new ArrayList<>();
        for (DetalleVentaDto detalleVentaDto : detalleVentaDtos) {
            detalleVentas.add(dtoToModel(detalleVentaDto));
        }
        
        return detalleVentas;
    }
}
