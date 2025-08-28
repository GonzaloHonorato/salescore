package com.salescore.salescore.core.ventas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salescore.salescore.core.ventas.dto.ProductoDto;
import com.salescore.salescore.core.ventas.mapper.ProductoMapper;
import com.salescore.salescore.core.ventas.model.Producto;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductoService {
    
    @Autowired
    private ProductoMapper productoMapper;
    
    private List<Producto> productos;
    private Long lastId;
    
    @PostConstruct
    public void init() {
        productos = new ArrayList<>();
        lastId = 0L;
        
        // Carga de datos iniciales
        crearProducto(new ProductoDto(null, "Alimento para perros premium", "Alimentos", 25000.0, 50, "Alimento premium para perros adultos - 15kg"));
        crearProducto(new ProductoDto(null, "Pelota para perros", "Juguetes", 5000.0, 30, "Pelota de goma resistente para perros"));
        crearProducto(new ProductoDto(null, "Correa para paseo", "Accesorios", 12000.0, 25, "Correa retráctil para paseos de hasta 5 metros"));
        crearProducto(new ProductoDto(null, "Cama para gatos", "Accesorios", 18000.0, 15, "Cama acolchada para gatos de tamaño mediano"));
        crearProducto(new ProductoDto(null, "Shampoo para mascotas", "Higiene", 8000.0, 40, "Shampoo hipoalergénico para perros y gatos"));
    }
    
    public List<ProductoDto> listarTodos() {
        return productoMapper.modelsToDtos(productos);
    }
    
    public Optional<ProductoDto> buscarPorId(Long id) {
        return productos.stream()
                .filter(producto -> producto.getId().equals(id))
                .findFirst()
                .map(producto -> productoMapper.modelToDto(producto));
    }
    
    public List<ProductoDto> buscarPorCategoria(String categoria) {
        return productos.stream()
                .filter(producto -> producto.getCategoria().equalsIgnoreCase(categoria))
                .map(producto -> productoMapper.modelToDto(producto))
                .collect(Collectors.toList());
    }
    
    public ProductoDto crearProducto(ProductoDto productoDto) {
        lastId++;
        productoDto.setId(lastId);
        
        Producto producto = productoMapper.dtoToModel(productoDto);
        productos.add(producto);
        
        return productoMapper.modelToDto(producto);
    }
    
    public boolean actualizarStock(Long id, int cantidad) {
        Optional<Producto> productoOpt = productos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
                
        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            int nuevoStock = producto.getStock() - cantidad;
            
            if (nuevoStock >= 0) {
                producto.setStock(nuevoStock);
                return true;
            }
        }
        
        return false;
    }
}
