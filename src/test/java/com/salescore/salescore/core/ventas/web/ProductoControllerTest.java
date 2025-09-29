package com.salescore.salescore.core.ventas.web;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.salescore.salescore.core.ventas.dto.ProductoDto;
import com.salescore.salescore.core.ventas.service.ProductoService;

@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    private ProductoDto productoDto;
    private List<ProductoDto> productoDtoList;

    @BeforeEach
    void setUp() {
        productoDto = new ProductoDto();
        productoDto.setId(1);
        productoDto.setNombre("Producto Test");
        productoDto.setCategoria("Categoria Test");
        productoDto.setPrecio(100.0);
        productoDto.setStock(10);

        ProductoDto productoDto2 = new ProductoDto();
        productoDto2.setId(2);
        productoDto2.setNombre("Producto Test 2");
        productoDto2.setCategoria("Categoria Test");
        productoDto2.setPrecio(200.0);
        productoDto2.setStock(20);

        productoDtoList = Arrays.asList(productoDto, productoDto2);
    }

    @Test
    void testObtenerProductos() throws Exception {
        when(productoService.listarTodos()).thenReturn(productoDtoList);

        mockMvc.perform(get("/api/productos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.productoDtoList[0].nombre").value("Producto Test"))
                .andExpect(jsonPath("$._embedded.productoDtoList[1].nombre").value("Producto Test 2"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void testObtenerProductoPorId() throws Exception {
        when(productoService.buscarPorId(eq(1))).thenReturn(Optional.of(productoDto));

        mockMvc.perform(get("/api/productos/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Producto Test"))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.productos.href").exists())
                .andExpect(jsonPath("$._links['misma-categoria'].href").exists());
    }
}