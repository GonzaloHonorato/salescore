package com.salescore.salescore.core.ventas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salescore.salescore.core.ventas.dto.DetalleVentaDto;
import com.salescore.salescore.core.ventas.dto.ProductoDto;
import com.salescore.salescore.core.ventas.dto.VentaDto;

import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DatosIniciales {

    @Autowired
    private ProductoService productoService;
    
    @Autowired
    private VentaService ventaService;
    
    @PostConstruct
    public void inicializarDatos() {
        // Los productos ya se inicializan en ProductoService
        
        // Crear algunas ventas de ejemplo
        crearVentasEjemplo();
    }
    
    private void crearVentasEjemplo() {
        // Obtenemos los productos disponibles
        List<ProductoDto> productos = productoService.listarTodos();
        
        // Primera venta - Hace 2 días
        VentaDto venta1 = new VentaDto();
        venta1.setCliente("Juan Pérez");
        venta1.setFechaVenta(LocalDateTime.now().minusDays(2));
        
        DetalleVentaDto detalle1 = new DetalleVentaDto();
        detalle1.setId(1L);
        detalle1.setProducto(productos.get(0));  // Alimento para perros
        detalle1.setCantidad(2);
        detalle1.setPrecioUnitario(productos.get(0).getPrecio());
        
        DetalleVentaDto detalle2 = new DetalleVentaDto();
        detalle2.setId(2L);
        detalle2.setProducto(productos.get(2));  // Correa para paseo
        detalle2.setCantidad(1);
        detalle2.setPrecioUnitario(productos.get(2).getPrecio());
        
        venta1.setDetalles(new ArrayList<>());
        venta1.addDetalle(detalle1);
        venta1.addDetalle(detalle2);
        
        try {
            ventaService.registrarVenta(venta1);
        } catch (Exception e) {
            System.err.println("Error al crear venta 1: " + e.getMessage());
        }
        
        // Segunda venta - Ayer
        VentaDto venta2 = new VentaDto();
        venta2.setCliente("María Rodríguez");
        venta2.setFechaVenta(LocalDateTime.now().minusDays(1));
        
        DetalleVentaDto detalle3 = new DetalleVentaDto();
        detalle3.setId(1L);
        detalle3.setProducto(productos.get(3));  // Cama para gatos
        detalle3.setCantidad(1);
        detalle3.setPrecioUnitario(productos.get(3).getPrecio());
        
        DetalleVentaDto detalle4 = new DetalleVentaDto();
        detalle4.setId(2L);
        detalle4.setProducto(productos.get(4));  // Shampoo para mascotas
        detalle4.setCantidad(2);
        detalle4.setPrecioUnitario(productos.get(4).getPrecio());
        
        venta2.setDetalles(new ArrayList<>());
        venta2.addDetalle(detalle3);
        venta2.addDetalle(detalle4);
        
        try {
            ventaService.registrarVenta(venta2);
        } catch (Exception e) {
            System.err.println("Error al crear venta 2: " + e.getMessage());
        }
        
        // Tercera venta - Hoy
        VentaDto venta3 = new VentaDto();
        venta3.setCliente("Carlos Gómez");
        venta3.setFechaVenta(LocalDateTime.now());
        
        DetalleVentaDto detalle5 = new DetalleVentaDto();
        detalle5.setId(1L);
        detalle5.setProducto(productos.get(1));  // Pelota para perros
        detalle5.setCantidad(3);
        detalle5.setPrecioUnitario(productos.get(1).getPrecio());
        
        DetalleVentaDto detalle6 = new DetalleVentaDto();
        detalle6.setId(2L);
        detalle6.setProducto(productos.get(0));  // Alimento para perros
        detalle6.setCantidad(1);
        detalle6.setPrecioUnitario(productos.get(0).getPrecio());
        
        venta3.setDetalles(new ArrayList<>());
        venta3.addDetalle(detalle5);
        venta3.addDetalle(detalle6);
        
        try {
            ventaService.registrarVenta(venta3);
        } catch (Exception e) {
            System.err.println("Error al crear venta 3: " + e.getMessage());
        }
    }
}
