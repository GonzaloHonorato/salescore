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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.salescore.salescore.core.ventas.dto.VentaDto;
import com.salescore.salescore.core.ventas.mapper.VentaMapper;
import com.salescore.salescore.core.ventas.model.Venta;
import com.salescore.salescore.core.ventas.repository.VentaRepository;
import com.salescore.salescore.core.ventas.repository.DetalleVentaRepository;

/**
 * Pruebas unitarias para VentaService
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
public class VentaServiceTest {
    
    @Mock
    private VentaRepository ventaRepository;
    
    @Mock
    private DetalleVentaRepository detalleVentaRepository;
    
    @Mock
    private VentaMapper ventaMapper;
    
    @Mock
    private ProductoService productoService;
    
    @InjectMocks
    private VentaService ventaService;
    
    private Venta ventaEjemplo;
    private VentaDto ventaDtoEjemplo;
    private List<Venta> listVentas;
    
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
        
        listVentas = Arrays.asList(ventaEjemplo);
    }
    
    /**
     * Prueba unitaria para el método listarTodas()
     * 
     * Verifica que:
     * 1. Se llame al repositorio para obtener todas las ventas con detalles
     * 2. Se mapeé correctamente cada venta a DTO
     * 3. Se retorne la lista esperada de VentaDto
     * 
     * Escenario: Consulta exitosa de todas las ventas
     * Entrada: Sin parámetros
     * Salida esperada: Lista de VentaDto con los datos mapeados
     */
    @Test
    public void testListarTodas_DeberiaRetornarListaDeVentas() {
        // Arrange (Preparar)
        when(ventaRepository.findAllWithDetalles()).thenReturn(listVentas);
        when(ventaMapper.toDto(ventaEjemplo)).thenReturn(ventaDtoEjemplo);
        
        // Act (Actuar)
        List<VentaDto> resultado = ventaService.listarTodas();
        
        // Assert (Verificar)
        assertNotNull(resultado, "El resultado no debe ser null");
        assertEquals(1, resultado.size(), "Debe retornar exactamente 1 venta");
        assertEquals(ventaDtoEjemplo.getId(), resultado.get(0).getId(), "El ID debe coincidir");
        assertEquals(ventaDtoEjemplo.getTotal(), resultado.get(0).getTotal(), "El total debe coincidir");
        
        // Verificar que se llamaron los métodos correctos
        verify(ventaRepository, times(1)).findAllWithDetalles();
        verify(ventaMapper, times(1)).toDto(ventaEjemplo);
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
        // Arrange
        Integer idBuscado = 1;
        when(ventaRepository.findByIdWithDetalles(idBuscado)).thenReturn(Optional.of(ventaEjemplo));
        when(ventaMapper.toDto(ventaEjemplo)).thenReturn(ventaDtoEjemplo);
        
        // Act
        Optional<VentaDto> resultado = ventaService.buscarPorId(idBuscado);
        
        // Assert
        assertTrue(resultado.isPresent(), "Debe encontrar la venta");
        assertEquals(ventaDtoEjemplo.getId(), resultado.get().getId(), "El ID debe coincidir");
        assertEquals(ventaDtoEjemplo.getTotal(), resultado.get().getTotal(), "El total debe coincidir");
        
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
     * Método que se ejecuta después de cada prueba
     * Limpia recursos si es necesario
     */
    @org.junit.jupiter.api.AfterEach
    public void tearDown() {
        // Limpiar recursos si es necesario
        reset(ventaRepository, ventaMapper, productoService, detalleVentaRepository);
    }
}