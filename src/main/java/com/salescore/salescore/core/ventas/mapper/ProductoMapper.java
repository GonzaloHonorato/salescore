package com.salescore.salescore.core.ventas.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.salescore.salescore.core.ventas.dto.ProductoDto;
import com.salescore.salescore.core.ventas.model.Producto;

@Component
public class ProductoMapper {
    
    // Nuevos métodos para compatibilidad con JPA
    public ProductoDto toDto(Producto producto) {
        return modelToDto(producto);
    }
    
    public Producto toEntity(ProductoDto productoDto) {
        return dtoToModel(productoDto);
    }
    
    public ProductoDto modelToDto(Producto producto) {
        if (producto == null) {
            return null;
        }
        
        return new ProductoDto(
                producto.getId(),
                producto.getNombre(),
                producto.getCategoria(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getDescripcion()
        );
    }
    
    public Producto dtoToModel(ProductoDto productoDto) {
        if (productoDto == null) {
            return null;
        }
        
        return new Producto(
                productoDto.getId(),
                productoDto.getNombre(),
                productoDto.getCategoria(),
                productoDto.getPrecio(),
                productoDto.getStock(),
                productoDto.getDescripcion()
        );
    }
    
    public List<ProductoDto> modelsToDtos(List<Producto> productos) {
        if (productos == null) {
            return null;
        }
        
        List<ProductoDto> productoDtos = new ArrayList<>();
        for (Producto producto : productos) {
            productoDtos.add(modelToDto(producto));
        }
        
        return productoDtos;
    }
    
    public List<Producto> dtosToModels(List<ProductoDto> productoDtos) {
        if (productoDtos == null) {
            return null;
        }
        
        List<Producto> productos = new ArrayList<>();
        for (ProductoDto productoDto : productoDtos) {
            productos.add(dtoToModel(productoDto));
        }
        
        return productos;
    }
}
