package com.salescore.salescore.core.ventas.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.salescore.salescore.core.ventas.dto.VentaDto;
import com.salescore.salescore.core.ventas.service.VentaService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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
    public ResponseEntity<VentaDto> buscarPorId(@PathVariable Long id) {
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
}
