package com.salescore.salescore.core.ventas.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.salescore.salescore.core.ventas.model.Venta;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Integer> {
    
    @Query("SELECT v FROM Venta v LEFT JOIN FETCH v.detalles d LEFT JOIN FETCH d.producto")
    List<Venta> findAllWithDetalles();
    
    @Query("SELECT v FROM Venta v LEFT JOIN FETCH v.detalles d LEFT JOIN FETCH d.producto WHERE v.id = :id")
    Optional<Venta> findByIdWithDetalles(@Param("id") Integer id);
    
    @Query("SELECT v FROM Venta v WHERE v.fecha BETWEEN :fechaInicio AND :fechaFin")
    List<Venta> findByRangoFechas(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                  @Param("fechaFin") LocalDateTime fechaFin);
    
    List<Venta> findByClienteRut(String clienteRut);
    
    List<Venta> findByVendedor(String vendedor);
    
    @Query("SELECT v FROM Venta v WHERE v.total >= :montoMinimo")
    List<Venta> findByMontoMinimo(@Param("montoMinimo") Double montoMinimo);
    
    @Query(value = "SELECT COUNT(*) FROM ADMIN.ventas", nativeQuery = true)
    Long countVentasNative();
    
    @Query(value = "SELECT * FROM ADMIN.ventas", nativeQuery = true)
    List<Object[]> findAllNative();
}
