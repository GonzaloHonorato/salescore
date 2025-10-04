package com.salescore.salescore.core.ventas.web;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.salescore.salescore.core.ventas.dto.VentaDto;
import com.salescore.salescore.core.ventas.service.VentaService;

/**
 * Pruebas unitarias simplificadas para VentaController
 * 
 * Esta clase contiene pruebas unitarias que verifican el correcto
 * funcionamiento del controlador de ventas sin necesidad de 
 * cargar el contexto completo de Spring.
 * 
 * Utiliza Mockito para simular las dependencias.
 * 
 * @author Sistema de Ventas
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
public class VentaControllerSimpleTest {
    
    @Mock
    private VentaService ventaService;
    
    @InjectMocks
    private VentaController ventaController;
    
    private VentaDto ventaDto;
    
    /**
     * Configuración inicial para cada prueba
     * Se ejecuta antes de cada método de prueba (@Test)
     */
    @BeforeEach
    public void setUp() {
        // Configurar datos de prueba
        ventaDto = new VentaDto();
        ventaDto.setId(1);
        ventaDto.setFecha(LocalDateTime.now());
        ventaDto.setTotal(1500.0);
        ventaDto.setClienteNombre("Juan Pérez");
        ventaDto.setClienteRut("12345678-9");
        ventaDto.setVendedor("María González");
    }
    
    /**
     * Prueba unitaria para buscarPorId con ID existente
     * 
     * Verifica que:
     * 1. El controlador llame al servicio correctamente
     * 2. Retorne un ResponseEntity con status 200
     * 3. El contenido sea un EntityModel con la venta
     * 4. Se incluyan enlaces HATEOAS
     * 
     * Escenario: Búsqueda exitosa de una venta específica
     * Entrada: ID = 1
     * Salida esperada: ResponseEntity con EntityModel<VentaDto>
     */
    @Test
    public void testBuscarPorId_ConIdExistente_DeberiaRetornarVentaConHATEOAS() {
        // Arrange
        Integer idVenta = 1;
        when(ventaService.buscarPorId(idVenta)).thenReturn(Optional.of(ventaDto));
        
        // Act
        ResponseEntity<EntityModel<VentaDto>> response = ventaController.buscarPorId(idVenta);
        
        // Assert
        assertNotNull(response, "La respuesta no debe ser null");
        assertTrue(response.getStatusCode().is2xxSuccessful(), "El status debe ser 2xx");
        assertNotNull(response.getBody(), "El body no debe ser null");
        
        EntityModel<VentaDto> entityModel = response.getBody();
        assertNotNull(entityModel.getContent(), "El contenido no debe ser null");
        assertEquals(ventaDto.getId(), entityModel.getContent().getId(), "El ID debe coincidir");
        assertEquals(ventaDto.getClienteNombre(), entityModel.getContent().getClienteNombre(), "El cliente debe coincidir");
        
        // Verificar que se incluyan enlaces HATEOAS
        assertTrue(entityModel.hasLinks(), "Debe tener enlaces HATEOAS");
        assertTrue(entityModel.hasLink("self"), "Debe tener enlace self");
        
        // Verificar que se llamó al servicio
        verify(ventaService, times(1)).buscarPorId(idVenta);
    }
    
    /**
     * Prueba unitaria para buscarPorId con ID inexistente
     * 
     * Verifica que:
     * 1. El controlador llame al servicio correctamente
     * 2. Retorne un ResponseEntity con status 404
     * 3. No haya contenido en la respuesta
     * 
     * Escenario: Búsqueda de una venta que no existe
     * Entrada: ID = 999
     * Salida esperada: ResponseEntity con status 404
     */
    @Test
    public void testBuscarPorId_ConIdInexistente_DeberiaRetornar404() {
        // Arrange
        Integer idInexistente = 999;
        when(ventaService.buscarPorId(idInexistente)).thenReturn(Optional.empty());
        
        // Act
        ResponseEntity<EntityModel<VentaDto>> response = ventaController.buscarPorId(idInexistente);
        
        // Assert
        assertNotNull(response, "La respuesta no debe ser null");
        assertTrue(response.getStatusCode().is4xxClientError(), "El status debe ser 4xx");
        assertNull(response.getBody(), "El body debe ser null para 404");
        
        // Verificar que se llamó al servicio
        verify(ventaService, times(1)).buscarPorId(idInexistente);
    }
    
    /**
     * Prueba unitaria para listarTodas
     * 
     * Verifica que:
     * 1. El controlador llame al servicio correctamente
     * 2. Retorne un ResponseEntity con status 200
     * 3. El contenido sea un CollectionModel con las ventas
     * 4. Se incluyan enlaces HATEOAS de navegación
     * 
     * Escenario: Consulta exitosa de todas las ventas
     * Entrada: Ninguna
     * Salida esperada: CollectionModel con ventas y enlaces
     */
    @Test
    public void testListarTodas_DeberiaRetornarVentasConHATEOAS() {
        // Arrange
        List<VentaDto> listaVentas = Arrays.asList(ventaDto);
        when(ventaService.listarTodas()).thenReturn(listaVentas);
        
        // Act
        ResponseEntity<CollectionModel<EntityModel<VentaDto>>> response = ventaController.listarTodas();
        
        // Assert
        assertNotNull(response, "La respuesta no debe ser null");
        assertTrue(response.getStatusCode().is2xxSuccessful(), "El status debe ser 2xx");
        assertNotNull(response.getBody(), "El body no debe ser null");
        
        CollectionModel<EntityModel<VentaDto>> collectionModel = response.getBody();
        assertNotNull(collectionModel.getContent(), "El contenido no debe ser null");
        assertFalse(collectionModel.getContent().isEmpty(), "Debe tener al menos una venta");
        
        // Verificar que se incluyan enlaces HATEOAS
        assertTrue(collectionModel.hasLinks(), "Debe tener enlaces HATEOAS");
        assertTrue(collectionModel.hasLink("self"), "Debe tener enlace self");
        
        // Verificar que cada venta tenga sus propios enlaces
        EntityModel<VentaDto> primeraVenta = collectionModel.getContent().iterator().next();
        assertTrue(primeraVenta.hasLinks(), "Cada venta debe tener enlaces");
        
        // Verificar que se llamó al servicio
        verify(ventaService, times(1)).listarTodas();
    }
    
    /**
     * Método que se ejecuta después de cada prueba
     * Limpia y resetea los mocks
     */
    @AfterEach
    public void tearDown() {
        reset(ventaService);
    }
}