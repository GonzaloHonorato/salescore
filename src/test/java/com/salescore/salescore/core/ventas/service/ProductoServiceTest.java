


package com.salescore.salescore.core.ventas.service;

import com.salescore.salescore.core.ventas.model.Producto;
import com.salescore.salescore.core.ventas.repository.ProductoRepository;
import com.salescore.salescore.core.ventas.mapper.ProductoMapper;
import com.salescore.salescore.core.ventas.dto.ProductoDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class ProductoServiceTest {
    private ProductoRepository productoRepository;
    private ProductoMapper productoMapper;
    private ProductoService productoService;

    @BeforeEach
    void setUp() {
        productoRepository = Mockito.mock(ProductoRepository.class);
        productoMapper = Mockito.mock(ProductoMapper.class);
        productoService = new ProductoService();
        // Inyectar mocks manualmente
        productoService.productoRepository = productoRepository;
        productoService.productoMapper = productoMapper;
    }

    @Test
    void testBuscarPorId() {
        Producto producto = new Producto();
        producto.setId(1);
        producto.setNombre("Test");
        ProductoDto productoDto = new ProductoDto();
        productoDto.setId(1);
        productoDto.setNombre("Test");
        Mockito.when(productoRepository.findById(1)).thenReturn(Optional.of(producto));
        Mockito.when(productoMapper.toDto(producto)).thenReturn(productoDto);
        Optional<ProductoDto> resultado = productoService.buscarPorId(1);
        assertTrue(resultado.isPresent());
        assertEquals("Test", resultado.get().getNombre());
    }
}
