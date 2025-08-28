package com.salescore.salescore.core.ventas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salescore.salescore.core.ventas.dto.DetalleVentaDto;
import com.salescore.salescore.core.ventas.dto.ReporteDto;
import com.salescore.salescore.core.ventas.dto.VentaDto;
import com.salescore.salescore.core.ventas.dto.VentaItemDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReporteService {
    
    @Autowired
    private VentaService ventaService;
    
    public ReporteDto generarReporteDiario(LocalDate fecha) {
        LocalDateTime inicio = fecha.atStartOfDay();
        LocalDateTime fin = fecha.atTime(LocalTime.MAX);
        return generarReporte(inicio, fin, fecha, fecha);
    }
    
    public ReporteDto generarReporteMensual(int año, int mes) {
        LocalDate inicio = LocalDate.of(año, mes, 1);
        LocalDate fin = inicio.plusMonths(1).minusDays(1);
        
        return generarReporte(
                inicio.atStartOfDay(),
                fin.atTime(LocalTime.MAX),
                inicio,
                fin
        );
    }
    
    public ReporteDto generarReporteAnual(int año) {
        LocalDate inicio = LocalDate.of(año, 1, 1);
        LocalDate fin = LocalDate.of(año, 12, 31);
        
        return generarReporte(
                inicio.atStartOfDay(),
                fin.atTime(LocalTime.MAX),
                inicio,
                fin
        );
    }
    
    private ReporteDto generarReporte(LocalDateTime fechaInicio, LocalDateTime fechaFin, LocalDate inicioDate, LocalDate finDate) {
        List<VentaDto> ventas = ventaService.buscarPorFechas(fechaInicio, fechaFin);
        
        ReporteDto reporte = new ReporteDto();
        reporte.setFechaInicio(inicioDate);
        reporte.setFechaFin(finDate);
        reporte.setVentas(ventas);
        
        // Calcular total de ventas
        double totalVentas = ventas.stream()
                .mapToDouble(VentaDto::getTotal)
                .sum();
        reporte.setTotalVentas(totalVentas);
        
        // Calcular ventas por producto
        Map<String, Double> ventasPorProductoMap = new HashMap<>();
        for (VentaDto venta : ventas) {
            for (DetalleVentaDto detalle : venta.getDetalles()) {
                String nombreProducto = detalle.getProducto().getNombre();
                double subtotal = detalle.getCantidad() * detalle.getPrecioUnitario();
                
                ventasPorProductoMap.put(
                        nombreProducto,
                        ventasPorProductoMap.getOrDefault(nombreProducto, 0.0) + subtotal
                );
            }
        }
        
        // Convertir el mapa a una lista de VentaItemDto
        List<VentaItemDto> ventasPorProducto = new ArrayList<>();
        for (Map.Entry<String, Double> entry : ventasPorProductoMap.entrySet()) {
            ventasPorProducto.add(new VentaItemDto(entry.getKey(), entry.getValue()));
        }
        reporte.setVentasPorProducto(ventasPorProducto);
        
        // Calcular ventas por categoría
        Map<String, Double> ventasPorCategoriaMap = new HashMap<>();
        for (VentaDto venta : ventas) {
            for (DetalleVentaDto detalle : venta.getDetalles()) {
                String categoria = detalle.getProducto().getCategoria();
                double subtotal = detalle.getCantidad() * detalle.getPrecioUnitario();
                
                ventasPorCategoriaMap.put(
                        categoria,
                        ventasPorCategoriaMap.getOrDefault(categoria, 0.0) + subtotal
                );
            }
        }
        
        // Convertir el mapa a una lista de VentaItemDto
        List<VentaItemDto> ventasPorCategoria = new ArrayList<>();
        for (Map.Entry<String, Double> entry : ventasPorCategoriaMap.entrySet()) {
            ventasPorCategoria.add(new VentaItemDto(entry.getKey(), entry.getValue()));
        }
        reporte.setVentasPorCategoria(ventasPorCategoria);
        
        return reporte;
    }
    
    public ReporteDto generarReportePorProducto(Long productoId) {
        List<VentaDto> todasLasVentas = ventaService.listarTodas();
        
        // Filtrar ventas que contienen el producto especificado
        List<VentaDto> ventasConProducto = todasLasVentas.stream()
                .filter(venta -> venta.getDetalles().stream()
                        .anyMatch(detalle -> detalle.getProducto().getId().equals(productoId)))
                .collect(Collectors.toList());
        
        // Crear reporte
        ReporteDto reporte = new ReporteDto();
        reporte.setVentas(ventasConProducto);
        
        // Calcular total de ventas del producto
        double totalVentasProducto = ventasConProducto.stream()
                .flatMap(venta -> venta.getDetalles().stream())
                .filter(detalle -> detalle.getProducto().getId().equals(productoId))
                .mapToDouble(detalle -> detalle.getCantidad() * detalle.getPrecioUnitario())
                .sum();
        
        reporte.setTotalVentas(totalVentasProducto);
        
        return reporte;
    }
}
