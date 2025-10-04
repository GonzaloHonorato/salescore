package com.salescore.salescore.core.ventas.service;

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

import com.salescore.salescore.core.ventas.dto.VentaDto;
import com.salescore.salescore.core.ventas.mapper.VentaMapper;
import com.salescore.salescore.core.ventas.model.Venta;
import com.salescore.salescore.core.ventas.repository.VentaRepository;

/**
 * Pruebas unitarias simplificadas para VentaService
 * 
 * Esta clase contiene las pruebas unitarias que verifican el correcto
 * funcionamiento de los métodos del servicio de ventas.
 * 
 * Utiliza Mockito para simular las dependencias y JUnit 5 para 
 * las aserciones y estructura de las pruebas.
 * 
 * @author Sistema de Ventas
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
public class VentaServiceSimpleTest {
    
    @Mock
    private VentaRepository ventaRepository;
    
    @Mock
    private VentaMapper ventaMapper;
    
    @Mock
    private ProductoService productoService;
    
    @InjectMocks
    private VentaService ventaService;
    
    private Venta ventaEjemplo;
    private VentaDto ventaDtoEjemplo;
    
    /**
     * Configuración inicial para cada prueba
     * Se ejecuta antes de cada método de prueba (@Test)
     */
    @BeforeEach
    public void setUp() {
        // Configurar datos de prueba
        ventaEjemplo = new Venta();
        ventaEjemplo.setId(1);
        ventaEjemplo.setFecha(LocalDateTime.now());
        ventaEjemplo.setTotal(1000.0);
        ventaEjemplo.setClienteNombre("Cliente Test");
        ventaEjemplo.setVendedor("Vendedor Test");
        
        ventaDtoEjemplo = new VentaDto();
        ventaDtoEjemplo.setId(1);
        ventaDtoEjemplo.setFecha(LocalDateTime.now());
        ventaDtoEjemplo.setTotal(1000.0);
        ventaDtoEjemplo.setClienteNombre("Cliente Test");
        ventaDtoEjemplo.setVendedor("Vendedor Test");
    }
    
    /**
     * Prueba unitaria para el método buscarPorId()
     * 
     * Verifica que:
     * 1. Se llame al repositorio con el ID correcto
     * 2. Se mapeé la venta encontrada a DTO
     * 3. Se retorne un Optional con el VentaDto
     * 
     * Escenario: Búsqueda exitosa de una venta por ID existente
     * Entrada: ID = 1
     * Salida esperada: Optional<VentaDto> con los datos de la venta
     */
    @Test
    public void testBuscarPorId_ConIdExistente_DeberiaRetornarVenta() {
        // Arrange (Preparar)
        Integer idBuscado = 1;
        when(ventaRepository.findByIdWithDetalles(idBuscado)).thenReturn(Optional.of(ventaEjemplo));
        when(ventaMapper.toDto(ventaEjemplo)).thenReturn(ventaDtoEjemplo);
        
        // Act (Actuar)
        Optional<VentaDto> resultado = ventaService.buscarPorId(idBuscado);
        
        // Assert (Verificar)
        assertTrue(resultado.isPresent(), "Debe encontrar la venta");
        assertEquals(ventaDtoEjemplo.getId(), resultado.get().getId(), "El ID debe coincidir");
        assertEquals(ventaDtoEjemplo.getTotal(), resultado.get().getTotal(), "El total debe coincidir");
        assertEquals("Cliente Test", resultado.get().getClienteNombre(), "El cliente debe coincidir");
        
        // Verificar interacciones con mocks
        verify(ventaRepository, times(1)).findByIdWithDetalles(idBuscado);
        verify(ventaMapper, times(1)).toDto(ventaEjemplo);
    }
    
    /**
     * Prueba unitaria para buscarPorId con ID inexistente
     * 
     * Verifica que:
     * 1. Se llame al repositorio con el ID correcto
     * 2. Se retorne un Optional vacío cuando no existe la venta
     * 3. No se llame al mapper cuando no hay datos
     * 
     * Escenario: Búsqueda de una venta con ID que no existe
     * Entrada: ID = 999 (inexistente)
     * Salida esperada: Optional.empty()
     */
    @Test
    public void testBuscarPorId_ConIdInexistente_DeberiaRetornarOptionalVacio() {
        // Arrange
        Integer idInexistente = 999;
        when(ventaRepository.findByIdWithDetalles(idInexistente)).thenReturn(Optional.empty());
        
        // Act
        Optional<VentaDto> resultado = ventaService.buscarPorId(idInexistente);
        
        // Assert
        assertFalse(resultado.isPresent(), "No debe encontrar la venta");
        
        // Verificar que no se llama al mapper cuando no hay datos
        verify(ventaRepository, times(1)).findByIdWithDetalles(idInexistente);
        verify(ventaMapper, never()).toDto(any(Venta.class));
    }
    
    /**
     * Prueba unitaria para listarTodas()
     * 
     * Verifica que:
     * 1. Se llame al repositorio correctamente
     * 2. Se mapeén correctamente las ventas
     * 3. Se retorne una lista no vacía
     * 
     * Escenario: Consulta exitosa que retorna ventas
     */
    @Test
    public void testListarTodas_DeberiaRetornarListaDeVentas() {
        // Arrange
        List<Venta> listVentas = Arrays.asList(ventaEjemplo);
        when(ventaRepository.findAllWithDetalles()).thenReturn(listVentas);
        when(ventaMapper.toDto(ventaEjemplo)).thenReturn(ventaDtoEjemplo);
        
        // Act
        List<VentaDto> resultado = ventaService.listarTodas();
        
        // Assert
        assertNotNull(resultado, "El resultado no debe ser null");
        assertEquals(1, resultado.size(), "Debe retornar exactamente 1 venta");
        assertEquals(ventaDtoEjemplo.getId(), resultado.get(0).getId(), "El ID debe coincidir");
        assertEquals(ventaDtoEjemplo.getTotal(), resultado.get(0).getTotal(), "El total debe coincidir");
        
        // Verificar que se llamaron los métodos correctos
        verify(ventaRepository, times(1)).findAllWithDetalles();
        verify(ventaMapper, times(1)).toDto(ventaEjemplo);
    }
    
    /**
     * Método que se ejecuta después de cada prueba
     * Limpia recursos si es necesario
     */
    @AfterEach
    public void tearDown() {
        // Limpiar recursos si es necesario
        reset(ventaRepository, ventaMapper, productoService);
    }
}