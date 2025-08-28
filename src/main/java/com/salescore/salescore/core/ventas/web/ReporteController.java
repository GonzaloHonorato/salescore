package com.salescore.salescore.core.ventas.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.salescore.salescore.core.ventas.dto.ReporteDto;
import com.salescore.salescore.core.ventas.service.ReporteService;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {
    
    @Autowired
    private ReporteService reporteService;
    
    @GetMapping("/diario")
    public ResponseEntity<ReporteDto> generarReporteDiario(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(reporteService.generarReporteDiario(fecha));
    }
    
    @GetMapping("/mensual")
    public ResponseEntity<ReporteDto> generarReporteMensual(
            @RequestParam int año,
            @RequestParam int mes) {
        return ResponseEntity.ok(reporteService.generarReporteMensual(año, mes));
    }
    
    @GetMapping("/anual")
    public ResponseEntity<ReporteDto> generarReporteAnual(
            @RequestParam int año) {
        return ResponseEntity.ok(reporteService.generarReporteAnual(año));
    }
    
    @GetMapping("/producto/{productoId}")
    public ResponseEntity<ReporteDto> generarReportePorProducto(
            @PathVariable Long productoId) {
        return ResponseEntity.ok(reporteService.generarReportePorProducto(productoId));
    }
}
