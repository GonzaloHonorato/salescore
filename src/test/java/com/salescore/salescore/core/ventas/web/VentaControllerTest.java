package com.salescore.salescore.core.ventas.web;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.salescore.salescore.core.ventas.dto.VentaDto;
import com.salescore.salescore.core.ventas.service.VentaService;

/**
 * Pruebas unitarias para VentaController
 * 
 * Esta clase contiene las pruebas de integración que verifican el correcto
 * funcionamiento de los endpoints REST del controlador de ventas.
 * 
 * Utiliza MockMvc para simular peticiones HTTP y MockBean para
 * simular las dependencias del servicio.
 * 
 * Las pruebas verifican tanto respuestas exitosas como casos de error,
 * incluyendo la implementación de enlaces HATEOAS.
 * 
 * @author Sistema de Ventas
 * @version 1.0
 */
@WebMvcTest(controllers = VentaController.class)
@ActiveProfiles("test")
public class VentaControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private VentaService ventaService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private VentaDto ventaDto;
    private List<VentaDto> listaVentas;
    
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
        
        listaVentas = Arrays.asList(ventaDto);
    }
    
    /**
     * Prueba unitaria para GET /api/ventas
     * 
     * Verifica que:
     * 1. El endpoint responda con status 200 OK
     * 2. Retorne una estructura JSON con enlaces HATEOAS
     * 3. Contenga los datos de las ventas en el formato correcto
     * 4. Incluya enlaces de navegación (_links)
     * 
     * Escenario: Consulta exitosa de todas las ventas con HATEOAS
     * Entrada: GET /api/ventas
     * Salida esperada: CollectionModel con ventas y enlaces
     */
    @Test
    public void testListarTodas_DeberiaRetornarVentasConHATEOAS() throws Exception {
        // Arrange
        when(ventaService.listarTodas()).thenReturn(listaVentas);
        
        // Act & Assert
        mockMvc.perform(get("/api/ventas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._embedded.ventaDtoList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.ventaDtoList[0].id", is(1)))
                .andExpect(jsonPath("$._embedded.ventaDtoList[0].total", is(1500.0)))
                .andExpect(jsonPath("$._embedded.ventaDtoList[0].clienteNombre", is("Juan Pérez")))
                .andExpect(jsonPath("$._embedded.ventaDtoList[0]._links.self.href", containsString("/api/ventas/1")))
                .andExpect(jsonPath("$._embedded.ventaDtoList[0]._links.actualizar.href", containsString("/api/ventas/1")))
                .andExpect(jsonPath("$._embedded.ventaDtoList[0]._links.eliminar.href", containsString("/api/ventas/1")))
                .andExpect(jsonPath("$._embedded.ventaDtoList[0]._links.detalles.href", containsString("/api/detalles-venta/venta/1")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/ventas")))
                .andExpect(jsonPath("$._links.crear.href", containsString("/api/ventas")))
                .andExpect(jsonPath("$._links.ganancias-dia.href", containsString("/api/ventas/ganancias/dia")));
        
        // Verificar que se llamó al servicio
        verify(ventaService, times(1)).listarTodas();
    }
    
    /**
     * Prueba unitaria para GET /api/ventas/{id}
     * 
     * Verifica que:
     * 1. El endpoint responda con status 200 OK cuando existe la venta
     * 2. Retorne los datos correctos de la venta
     * 3. Incluya enlaces HATEOAS apropiados
     * 4. Los enlaces apunten a recursos relacionados correctos
     * 
     * Escenario: Búsqueda exitosa de una venta específica
     * Entrada: GET /api/ventas/1
     * Salida esperada: EntityModel con venta y enlaces HATEOAS
     */
    @Test
    public void testBuscarPorId_ConIdExistente_DeberiaRetornarVentaConHATEOAS() throws Exception {
        // Arrange
        Integer idVenta = 1;
        when(ventaService.buscarPorId(idVenta)).thenReturn(Optional.of(ventaDto));
        
        // Act & Assert
        mockMvc.perform(get("/api/ventas/{id}", idVenta)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.total", is(1500.0)))
                .andExpect(jsonPath("$.clienteNombre", is("Juan Pérez")))
                .andExpect(jsonPath("$.clienteRut", is("12345678-9")))
                .andExpect(jsonPath("$.vendedor", is("María González")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/ventas/1")))
                .andExpect(jsonPath("$._links.actualizar.href", containsString("/api/ventas/1")))
                .andExpect(jsonPath("$._links.eliminar.href", containsString("/api/ventas/1")))
                .andExpect(jsonPath("$._links.collection.href", containsString("/api/ventas")))
                .andExpect(jsonPath("$._links.detalles.href", containsString("/api/detalles-venta/venta/1")));
        
        // Verificar interacción con el servicio
        verify(ventaService, times(1)).buscarPorId(idVenta);
    }
    
    /**
     * Prueba unitaria para GET /api/ventas/{id} con ID inexistente
     * 
     * Verifica que:
     * 1. El endpoint responda con status 404 Not Found
     * 2. No retorne contenido cuando la venta no existe
     * 3. El servicio sea invocado correctamente
     * 
     * Escenario: Búsqueda de una venta que no existe
     * Entrada: GET /api/ventas/999
     * Salida esperada: 404 Not Found sin contenido
     */
    @Test
    public void testBuscarPorId_ConIdInexistente_DeberiaRetornar404() throws Exception {
        // Arrange
        Integer idInexistente = 999;
        when(ventaService.buscarPorId(idInexistente)).thenReturn(Optional.empty());
        
        // Act & Assert
        mockMvc.perform(get("/api/ventas/{id}", idInexistente)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        
        // Verificar que se llamó al servicio con el ID correcto
        verify(ventaService, times(1)).buscarPorId(idInexistente);
    }
    
    /**
     * Prueba unitaria para POST /api/ventas
     * 
     * Verifica que:
     * 1. El endpoint responda con status 201 Created
     * 2. Retorne la venta creada con enlaces HATEOAS
     * 3. El servicio sea invocado con los datos correctos
     * 4. Se incluyan todos los enlaces necesarios
     * 
     * Escenario: Creación exitosa de una nueva venta
     * Entrada: POST /api/ventas con datos válidos
     * Salida esperada: 201 Created con EntityModel
     */
    @Test
    public void testCrearVenta_ConDatosValidos_DeberiaRetornarVentaCreadaConHATEOAS() throws Exception {
        // Arrange
        VentaDto nuevaVenta = new VentaDto();
        nuevaVenta.setTotal(2000.0);
        nuevaVenta.setClienteNombre("Ana Silva");
        nuevaVenta.setVendedor("Carlos Ruiz");
        
        VentaDto ventaCreada = new VentaDto();
        ventaCreada.setId(2);
        ventaCreada.setFecha(LocalDateTime.now());
        ventaCreada.setTotal(2000.0);
        ventaCreada.setClienteNombre("Ana Silva");
        ventaCreada.setVendedor("Carlos Ruiz");
        
        when(ventaService.crear(any(VentaDto.class))).thenReturn(ventaCreada);
        
        // Act & Assert
        mockMvc.perform(post("/api/ventas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevaVenta)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.total", is(2000.0)))
                .andExpect(jsonPath("$.clienteNombre", is("Ana Silva")))
                .andExpect(jsonPath("$.vendedor", is("Carlos Ruiz")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/ventas/2")))
                .andExpect(jsonPath("$._links.actualizar.href", containsString("/api/ventas/2")))
                .andExpect(jsonPath("$._links.eliminar.href", containsString("/api/ventas/2")));
        
        // Verificar que se llamó al servicio de creación
        verify(ventaService, times(1)).crear(any(VentaDto.class));
    }
    
    /**
     * Método que se ejecuta después de cada prueba
     * Limpia y resetea los mocks
     */
    @org.junit.jupiter.api.AfterEach
    public void tearDown() {
        reset(ventaService);
    }
}