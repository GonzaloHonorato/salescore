package com.salescore.salescore.core.ventas.dto;

public class VentaItemDto {
    private String nombre;
    private Double total;
    
    public VentaItemDto() {
    }
    
    public VentaItemDto(String nombre, Double total) {
        this.nombre = nombre;
        this.total = total;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public Double getTotal() {
        return total;
    }
    
    public void setTotal(Double total) {
        this.total = total;
    }
}
