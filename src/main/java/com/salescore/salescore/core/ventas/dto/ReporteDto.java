package com.salescore.salescore.core.ventas.dto;

import java.time.LocalDate;
import java.util.List;

public class ReporteDto {
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Double totalVentas;
    private List<VentaDto> ventas;
    private List<VentaItemDto> ventasPorProducto;
    private List<VentaItemDto> ventasPorCategoria;
    
    public ReporteDto() {
    }
    
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }
    
    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
    
    public LocalDate getFechaFin() {
        return fechaFin;
    }
    
    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }
    
    public Double getTotalVentas() {
        return totalVentas;
    }
    
    public void setTotalVentas(Double totalVentas) {
        this.totalVentas = totalVentas;
    }
    
    public List<VentaDto> getVentas() {
        return ventas;
    }
    
    public void setVentas(List<VentaDto> ventas) {
        this.ventas = ventas;
    }
    
    public List<VentaItemDto> getVentasPorProducto() {
        return ventasPorProducto;
    }
    
    public void setVentasPorProducto(List<VentaItemDto> ventasPorProducto) {
        this.ventasPorProducto = ventasPorProducto;
    }
    
    public List<VentaItemDto> getVentasPorCategoria() {
        return ventasPorCategoria;
    }
    
    public void setVentasPorCategoria(List<VentaItemDto> ventasPorCategoria) {
        this.ventasPorCategoria = ventasPorCategoria;
    }
}
