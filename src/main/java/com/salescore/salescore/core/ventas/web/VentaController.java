package com.salescore.salescore.core.ventas.web;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.salescore.salescore.core.ventas.dto.VentaDto;
import com.salescore.salescore.core.ventas.service.VentaService;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {
    
    @Autowired
    private VentaService ventaService;
    
    @GetMapping
    public ResponseEntity<List<VentaDto>> listarTodas() {
        return ResponseEntity.ok(ventaService.listarTodas());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<VentaDto> buscarPorId(@PathVariable Integer id) {
        return ventaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/fecha")
    public ResponseEntity<List<VentaDto>> buscarPorFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        LocalDateTime inicio = fecha.atStartOfDay();
        LocalDateTime fin = fecha.atTime(LocalTime.MAX);
        
        return ResponseEntity.ok(ventaService.buscarPorFechas(inicio, fin));
    }
    
    @GetMapping("/periodo")
    public ResponseEntity<List<VentaDto>> buscarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin.atTime(LocalTime.MAX);
        
        return ResponseEntity.ok(ventaService.buscarPorFechas(inicio, fin));
    }
    
    @GetMapping("/ganancias/dia")
    public ResponseEntity<Double> obtenerGananciasDiarias(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        LocalDateTime inicio = fecha.atStartOfDay();
        LocalDateTime fin = fecha.atTime(LocalTime.MAX);
        
        double ganancias = ventaService.calcularGananciasPorPeriodo(inicio, fin);
        return ResponseEntity.ok(ganancias);
    }
    
    @GetMapping("/ganancias/mes")
    public ResponseEntity<Double> obtenerGananciasMensuales(
            @RequestParam int año,
            @RequestParam int mes) {
        LocalDate fechaInicio = LocalDate.of(año, mes, 1);
        LocalDate fechaFin = fechaInicio.plusMonths(1).minusDays(1);
        
        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin.atTime(LocalTime.MAX);
        
        double ganancias = ventaService.calcularGananciasPorPeriodo(inicio, fin);
        return ResponseEntity.ok(ganancias);
    }
    
    @GetMapping("/ganancias/año")
    public ResponseEntity<Double> obtenerGananciasAnuales(
            @RequestParam int año) {
        LocalDate fechaInicio = LocalDate.of(año, 1, 1);
        LocalDate fechaFin = LocalDate.of(año, 12, 31);
        
        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin.atTime(LocalTime.MAX);
        
        double ganancias = ventaService.calcularGananciasPorPeriodo(inicio, fin);
        return ResponseEntity.ok(ganancias);
    }
    
    @PostMapping
    public ResponseEntity<VentaDto> crearVenta(@RequestBody VentaDto ventaDto) {
        try {
            VentaDto creada = ventaService.crear(ventaDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(creada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<VentaDto> actualizarVenta(@PathVariable Integer id, @RequestBody VentaDto ventaDto) {
        try {
            VentaDto actualizada = ventaService.actualizar(id, ventaDto);
            return ResponseEntity.ok(actualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarVenta(@PathVariable Integer id) {
        try {
            ventaService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Endpoints de debug
    @GetMapping("/debug/count")
    public ResponseEntity<Long> contarVentasNativo() {
        Long count = ventaService.contarVentasNativo();
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/debug/raw")
    public ResponseEntity<List<Object[]>> obtenerVentasNativo() {
        List<Object[]> ventas = ventaService.obtenerVentasNativo();
        return ResponseEntity.ok(ventas);
    }
}
