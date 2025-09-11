package com.salescore.salescore.core.ventas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.salescore.salescore.core.ventas.model.DetalleVenta;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Integer> {
    
    List<DetalleVenta> findByVentaId(Integer ventaId);
    
    List<DetalleVenta> findByProductoId(Integer productoId);
    
    @Query("SELECT d FROM DetalleVenta d WHERE d.cantidad > :cantidadMinima")
    List<DetalleVenta> findByCantidadMayorQue(@Param("cantidadMinima") Integer cantidadMinima);
    
    @Query("SELECT SUM(d.subtotal) FROM DetalleVenta d WHERE d.venta.id = :ventaId")
    Double sumSubtotalByVentaId(@Param("ventaId") Integer ventaId);
}
