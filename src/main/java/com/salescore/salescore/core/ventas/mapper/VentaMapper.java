package com.salescore.salescore.core.ventas.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salescore.salescore.core.ventas.dto.VentaDto;
import com.salescore.salescore.core.ventas.model.Venta;

import java.util.ArrayList;
import java.util.List;

@Component
public class VentaMapper {
    
    @Autowired
    private DetalleVentaMapper detalleVentaMapper;
    
    public VentaDto modelToDto(Venta venta) {
        if (venta == null) {
            return null;
        }
        
        VentaDto ventaDto = new VentaDto(
                venta.getId(),
                venta.getFechaVenta(),
                venta.getTotal(),
                venta.getCliente()
        );
        
        ventaDto.setDetalles(detalleVentaMapper.modelsToDtos(venta.getDetalles()));
        
        return ventaDto;
    }
    
    public Venta dtoToModel(VentaDto ventaDto) {
        if (ventaDto == null) {
            return null;
        }
        
        Venta venta = new Venta(
                ventaDto.getId(),
                ventaDto.getFechaVenta(),
                ventaDto.getTotal(),
                ventaDto.getCliente()
        );
        
        venta.setDetalles(detalleVentaMapper.dtosToModels(ventaDto.getDetalles()));
        
        return venta;
    }
    
    public List<VentaDto> modelsToDtos(List<Venta> ventas) {
        if (ventas == null) {
            return null;
        }
        
        List<VentaDto> ventaDtos = new ArrayList<>();
        for (Venta venta : ventas) {
            ventaDtos.add(modelToDto(venta));
        }
        
        return ventaDtos;
    }
    
    public List<Venta> dtosToModels(List<VentaDto> ventaDtos) {
        if (ventaDtos == null) {
            return null;
        }
        
        List<Venta> ventas = new ArrayList<>();
        for (VentaDto ventaDto : ventaDtos) {
            ventas.add(dtoToModel(ventaDto));
        }
        
        return ventas;
    }
}
