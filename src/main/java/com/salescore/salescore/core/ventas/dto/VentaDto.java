package com.salescore.salescore.core.ventas.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VentaDto {
    private Integer id;
    private LocalDateTime fecha;
    private Double total;
    private String clienteNombre;
    private String clienteRut;
    private String vendedor;
    private List<DetalleVentaDto> detalles;
    
    public VentaDto() {
        this.detalles = new ArrayList<>();
    }
    
    public VentaDto(Integer id, LocalDateTime fecha, Double total, String clienteNombre, 
                    String clienteRut, String vendedor) {
        this.id = id;
        this.fecha = fecha;
        this.total = total;
        this.clienteNombre = clienteNombre;
        this.clienteRut = clienteRut;
        this.vendedor = vendedor;
        this.detalles = new ArrayList<>();
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public LocalDateTime getFecha() {
        return fecha;
    }
    
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
    
    public Double getTotal() {
        return total;
    }
    
    public void setTotal(Double total) {
        this.total = total;
    }
    
    public String getClienteNombre() {
        return clienteNombre;
    }
    
    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }
    
    public String getClienteRut() {
        return clienteRut;
    }
    
    public void setClienteRut(String clienteRut) {
        this.clienteRut = clienteRut;
    }
    
    public String getVendedor() {
        return vendedor;
    }
    
    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }
    
    public List<DetalleVentaDto> getDetalles() {
        return detalles;
    }
    
    public void setDetalles(List<DetalleVentaDto> detalles) {
        this.detalles = detalles;
    }
}
