package com.salescore.salescore.core.ventas.dto;

public class DetalleVentaDto {
    private Long id;
    private ProductoDto producto;
    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;
    
    public DetalleVentaDto() {
    }
    
    public DetalleVentaDto(Long id, ProductoDto producto, Integer cantidad, Double precioUnitario) {
        this.id = id;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = cantidad * precioUnitario;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public ProductoDto getProducto() {
        return producto;
    }
    
    public void setProducto(ProductoDto producto) {
        this.producto = producto;
    }
    
    public Integer getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
    
    public Double getPrecioUnitario() {
        return precioUnitario;
    }
    
    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
    
    public Double getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }
}
