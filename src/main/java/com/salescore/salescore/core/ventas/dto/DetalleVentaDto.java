package com.salescore.salescore.core.ventas.dto;

public class DetalleVentaDto {
    private Integer id;
    private Integer productoId;
    private ProductoDto producto;
    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;
    
    public DetalleVentaDto() {
    }
    
    public DetalleVentaDto(Integer id, ProductoDto producto, Integer cantidad, Double precioUnitario) {
        this.id = id;
        this.producto = producto;
        this.productoId = producto != null ? producto.getId() : null;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = cantidad * precioUnitario;
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getProductoId() {
        return productoId;
    }
    
    public void setProductoId(Integer productoId) {
        this.productoId = productoId;
    }
    
    public ProductoDto getProducto() {
        return producto;
    }
    
    public void setProducto(ProductoDto producto) {
        this.producto = producto;
        this.productoId = producto != null ? producto.getId() : null;
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
