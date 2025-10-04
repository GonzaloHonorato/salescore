package com.salescore.salescore;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import com.salescore.salescore.core.ventas.service.VentaService;

import static org.mockito.Mockito.mock;

/**
 * Configuración de pruebas para el contexto de Spring Boot
 * 
 * Esta clase proporciona beans mock para las pruebas unitarias
 * y de integración, evitando conflictos de dependencias.
 */
@TestConfiguration
public class TestConfig {
    
    @Bean
    @Primary
    public VentaService ventaService() {
        return mock(VentaService.class);
    }
}