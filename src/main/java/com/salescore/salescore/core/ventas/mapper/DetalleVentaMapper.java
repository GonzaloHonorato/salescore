package com.salescore.salescore.core.ventas.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salescore.salescore.core.ventas.dto.DetalleVentaDto;
import com.salescore.salescore.core.ventas.model.DetalleVenta;

import java.util.ArrayList;
import java.util.List;

@Component
public class DetalleVentaMapper {
    
    @Autowired
    private ProductoMapper productoMapper;
    
    public DetalleVentaDto modelToDto(DetalleVenta detalleVenta) {
        if (detalleVenta == null) {
            return null;
        }
        
        return new DetalleVentaDto(
                detalleVenta.getId(),
                productoMapper.modelToDto(detalleVenta.getProducto()),
                detalleVenta.getCantidad(),
                detalleVenta.getPrecioUnitario()
        );
    }
    
    public DetalleVenta dtoToModel(DetalleVentaDto detalleVentaDto) {
        if (detalleVentaDto == null) {
            return null;
        }
        
        return new DetalleVenta(
                detalleVentaDto.getId(),
                productoMapper.dtoToModel(detalleVentaDto.getProducto()),
                detalleVentaDto.getCantidad(),
                detalleVentaDto.getPrecioUnitario()
        );
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
