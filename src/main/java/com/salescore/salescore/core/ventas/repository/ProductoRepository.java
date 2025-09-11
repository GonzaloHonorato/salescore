package com.salescore.salescore.core.ventas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.salescore.salescore.core.ventas.model.Producto;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    
    List<Producto> findByCategoria(String categoria);
    
    @Query("SELECT p FROM Producto p WHERE p.precio <= :precio")
    List<Producto> findByPrecioLessThanEqual(@Param("precio") Double precio);
    
    @Query("SELECT p FROM Producto p WHERE p.stock < :stockMinimo")
    List<Producto> findByStockBajo(@Param("stockMinimo") Integer stockMinimo);
}
