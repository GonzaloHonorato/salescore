package com.salescore.salescore.core.ventas.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Venta {
    private Long id;
    private LocalDateTime fechaVenta;
    private Double total;
    private String cliente;
    private List<DetalleVenta> detalles;
    
    public Venta() {
        this.detalles = new ArrayList<>();
    }
    
    public Venta(Long id, LocalDateTime fechaVenta, Double total, String cliente) {
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
    
    public List<DetalleVenta> getDetalles() {
        return detalles;
    }
    
    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
    }
    
    public void addDetalle(DetalleVenta detalle) {
        this.detalles.add(detalle);
    }
    
    public void calcularTotal() {
        this.total = detalles.stream()
                .mapToDouble(detalle -> detalle.getPrecioUnitario() * detalle.getCantidad())
                .sum();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Venta venta = (Venta) o;
        return Objects.equals(id, venta.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Venta{" +
                "id=" + id +
                ", fechaVenta=" + fechaVenta +
                ", total=" + total +
                ", cliente='" + cliente + '\'' +
                ", detalles=" + detalles +
                '}';
    }
}
