package com.salescore.salescore.core.ventas.repository;

import com.salescore.salescore.core.ventas.model.Producto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ProductoRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    
    @Autowired
    private ProductoRepository productoRepository;

    @Test
    void testGuardarYBuscarProducto() {
        // Crear un producto completo
        Producto producto = new Producto();
        producto.setNombre("ProductoTest");
        producto.setCategoria("CategoriaTest");
        producto.setPrecio(100.0);
        producto.setStock(10);
        producto.setDescripcion("Descripción de prueba");
        
        // Persistir usando TestEntityManager
        entityManager.persist(producto);
        entityManager.flush();
        
        // Buscar usando el repositorio
        Producto encontrado = productoRepository.findById(producto.getId()).orElse(null);
        
        // Verificar
        assertNotNull(encontrado);
        assertEquals("ProductoTest", encontrado.getNombre());
        assertEquals("CategoriaTest", encontrado.getCategoria());
        assertEquals(100.0, encontrado.getPrecio());
    }
}
