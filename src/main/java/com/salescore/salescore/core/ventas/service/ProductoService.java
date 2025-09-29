package com.salescore.salescore.core.ventas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.salescore.salescore.core.ventas.dto.ProductoDto;
import com.salescore.salescore.core.ventas.mapper.ProductoMapper;
import com.salescore.salescore.core.ventas.model.Producto;
import com.salescore.salescore.core.ventas.repository.ProductoRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductoService {
    
    @Autowired
    public ProductoMapper productoMapper;
    
    @Autowired
    public ProductoRepository productoRepository;
    
    public List<ProductoDto> listarTodos() {
        return productoRepository.findAll().stream()
                .map(productoMapper::toDto)
                .collect(Collectors.toList());
    }
    
    public Optional<ProductoDto> buscarPorId(Integer id) {
        return productoRepository.findById(id)
                .map(productoMapper::toDto);
    }
    
    public List<ProductoDto> buscarPorCategoria(String categoria) {
        return productoRepository.findByCategoria(categoria).stream()
                .map(productoMapper::toDto)
                .collect(Collectors.toList());
    }
    
    public List<ProductoDto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(productoMapper::toDto)
                .collect(Collectors.toList());
    }
    
    public ProductoDto crear(ProductoDto productoDto) {
        Producto producto = productoMapper.toEntity(productoDto);
        producto.setId(null); // Asegurar que es una nueva entidad
        Producto guardado = productoRepository.save(producto);
        return productoMapper.toDto(guardado);
    }
    
    public ProductoDto actualizar(Integer id, ProductoDto productoDto) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado con ID: " + id);
        }
        
        Producto producto = productoMapper.toEntity(productoDto);
        producto.setId(id);
        Producto actualizado = productoRepository.save(producto);
        return productoMapper.toDto(actualizado);
    }
    
    public void eliminar(Integer id) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado con ID: " + id);
        }
        productoRepository.deleteById(id);
    }
    
    public boolean actualizarStock(Integer id, int cantidad) {
        Optional<Producto> productoOpt = productoRepository.findById(id);
                
        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            int nuevoStock = producto.getStock() - cantidad;
            
            if (nuevoStock >= 0) {
                producto.setStock(nuevoStock);
                productoRepository.save(producto);
                return true;
            }
        }
        
        return false;
    }
}
