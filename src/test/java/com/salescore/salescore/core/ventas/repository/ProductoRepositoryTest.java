package com.salescore.salescore.core.ventas.repository;

import com.salescore.salescore.core.ventas.model.Producto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductoRepositoryTest {
    @Autowired
    private ProductoRepository productoRepository;

    @Test
    void testGuardarYBuscarProducto() {
        Producto producto = new Producto();
        producto.setNombre("ProductoTest");
        producto.setPrecio(100.0);
        Producto guardado = productoRepository.save(producto);
        assertNotNull(guardado.getId());
        Producto encontrado = productoRepository.findById(guardado.getId()).orElse(null);
        assertNotNull(encontrado);
        assertEquals("ProductoTest", encontrado.getNombre());
    }
}
