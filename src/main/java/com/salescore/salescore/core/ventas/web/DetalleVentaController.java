package com.salescore.salescore.core.ventas.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salescore.salescore.core.ventas.dto.DetalleVentaDto;
import com.salescore.salescore.core.ventas.service.DetalleVentaService;

@RestController
@RequestMapping("/api/detalles-venta")
public class DetalleVentaController {
    
    @Autowired
    private DetalleVentaService detalleVentaService;
    
    @GetMapping
    public ResponseEntity<List<DetalleVentaDto>> listarTodos() {
        return ResponseEntity.ok(detalleVentaService.listarTodos());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<DetalleVentaDto> buscarPorId(@PathVariable Integer id) {
        return detalleVentaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/venta/{ventaId}")
    public ResponseEntity<List<DetalleVentaDto>> buscarPorVenta(@PathVariable Integer ventaId) {
        return ResponseEntity.ok(detalleVentaService.buscarPorVenta(ventaId));
    }
    
    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<DetalleVentaDto>> buscarPorProducto(@PathVariable Integer productoId) {
        return ResponseEntity.ok(detalleVentaService.buscarPorProducto(productoId));
    }
    
    @PostMapping
    public ResponseEntity<DetalleVentaDto> crear(@RequestBody DetalleVentaDto detalleVentaDto) {
        try {
            DetalleVentaDto creado = detalleVentaService.crear(detalleVentaDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<DetalleVentaDto> actualizar(@PathVariable Integer id, @RequestBody DetalleVentaDto detalleVentaDto) {
        try {
            DetalleVentaDto actualizado = detalleVentaService.actualizar(id, detalleVentaDto);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        try {
            detalleVentaService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
