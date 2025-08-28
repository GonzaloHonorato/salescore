package com.salescore.salescore.core.ventas.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VentaDto {
    private Long id;
    private LocalDateTime fechaVenta;
    private Double total;
    private String cliente;
    private List<DetalleVentaDto> detalles;
    
    public VentaDto() {
        this.detalles = new ArrayList<>();
    }
    
    public VentaDto(Long id, LocalDateTime fechaVenta, Double total, String cliente) {
        this.id = id;
        this.fechaVenta = fechaVenta;
        this.total = total;
        this.cliente = cliente;
        this.detalles = new ArrayList<>();
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public LocalDateTime getFechaVenta() {
        return fechaVenta;
    }
    
    public void setFechaVenta(LocalDateTime fechaVenta) {
        this.fechaVenta = fechaVenta;
    }
    
    public Double getTotal() {
        return total;
    }
    
    public void setTotal(Double total) {
        this.total = total;
    }
    
    public String getCliente() {
        return cliente;
    }
    
    public void setCliente(String cliente) {
        this.cliente = cliente;
    }
    
    public List<DetalleVentaDto> getDetalles() {
        return detalles;
    }
    
    public void setDetalles(List<DetalleVentaDto> detalles) {
        this.detalles = detalles;
    }
    
    public void addDetalle(DetalleVentaDto detalle) {
        this.detalles.add(detalle);
    }
}
