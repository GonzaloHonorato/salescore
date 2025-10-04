package com.salescore.salescore.core.ventas.web;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.salescore.salescore.core.ventas.dto.VentaDto;
import com.salescore.salescore.core.ventas.service.VentaService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * Controlador REST para gestión de ventas con documentación HATEOAS
 * 
 * Proporciona endpoints para CRUD de ventas, consultas por fecha,
 * cálculo de ganancias y funcionalidades de debug.
 * 
 * Implementa hipermedia usando Spring HATEOAS para facilitar
 * la navegación de la API.
 */

@RestController
@RequestMapping("/api/ventas")
public class VentaController {
    
    @Autowired
    private VentaService ventaService;
    
    /**
     * Lista todas las ventas con enlaces HATEOAS
     * 
     * @return CollectionModel con todas las ventas y enlaces de navegación
     */
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<VentaDto>>> listarTodas() {
        List<VentaDto> ventas = ventaService.listarTodas();
        
        List<EntityModel<VentaDto>> ventasModel = ventas.stream()
            .map(this::agregarEnlaces)
            .collect(Collectors.toList());
        
        CollectionModel<EntityModel<VentaDto>> collectionModel = CollectionModel.of(ventasModel);
        
        // Agregar enlaces de navegación
        collectionModel.add(linkTo(methodOn(VentaController.class).listarTodas()).withSelfRel());
        collectionModel.add(linkTo(methodOn(VentaController.class).crearVenta(null)).withRel("crear"));
        collectionModel.add(linkTo(methodOn(VentaController.class).obtenerGananciasDiarias(LocalDate.now())).withRel("ganancias-dia"));
        
        return ResponseEntity.ok(collectionModel);
    }
    
    /**
     * Busca una venta por ID con enlaces HATEOAS
     * 
     * @param id ID de la venta a buscar
     * @return EntityModel con la venta y enlaces relacionados
     */
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<VentaDto>> buscarPorId(@PathVariable Integer id) {
        return ventaService.buscarPorId(id)
                .map(this::agregarEnlaces)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Agrega enlaces HATEOAS a una VentaDto
     * 
     * @param venta la venta a la que agregar enlaces
     * @return EntityModel con enlaces HATEOAS
     */
    private EntityModel<VentaDto> agregarEnlaces(VentaDto venta) {
        EntityModel<VentaDto> ventaModel = EntityModel.of(venta);
        
        // Enlace self
        ventaModel.add(linkTo(methodOn(VentaController.class).buscarPorId(venta.getId())).withSelfRel());
        
        // Enlace para actualizar
        ventaModel.add(linkTo(methodOn(VentaController.class).actualizarVenta(venta.getId(), null)).withRel("actualizar"));
        
        // Enlace para eliminar
        ventaModel.add(linkTo(methodOn(VentaController.class).eliminarVenta(venta.getId())).withRel("eliminar"));
        
        // Enlace para volver a la lista
        ventaModel.add(linkTo(methodOn(VentaController.class).listarTodas()).withRel(IanaLinkRelations.COLLECTION));
        
        // Enlace para ver detalles de la venta
        ventaModel.add(linkTo(methodOn(DetalleVentaController.class).buscarPorVenta(venta.getId())).withRel("detalles"));
        
        return ventaModel;
    }
    
    @GetMapping("/fecha")
    public ResponseEntity<List<VentaDto>> buscarPorFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        LocalDateTime inicio = fecha.atStartOfDay();
        LocalDateTime fin = fecha.atTime(LocalTime.MAX);
        
        return ResponseEntity.ok(ventaService.buscarPorFechas(inicio, fin));
    }
    
    @GetMapping("/periodo")
    public ResponseEntity<List<VentaDto>> buscarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin.atTime(LocalTime.MAX);
        
        return ResponseEntity.ok(ventaService.buscarPorFechas(inicio, fin));
    }
    
    @GetMapping("/ganancias/dia")
    public ResponseEntity<Double> obtenerGananciasDiarias(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        LocalDateTime inicio = fecha.atStartOfDay();
        LocalDateTime fin = fecha.atTime(LocalTime.MAX);
        
        double ganancias = ventaService.calcularGananciasPorPeriodo(inicio, fin);
        return ResponseEntity.ok(ganancias);
    }
    
    @GetMapping("/ganancias/mes")
    public ResponseEntity<Double> obtenerGananciasMensuales(
            @RequestParam int año,
            @RequestParam int mes) {
        LocalDate fechaInicio = LocalDate.of(año, mes, 1);
        LocalDate fechaFin = fechaInicio.plusMonths(1).minusDays(1);
        
        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin.atTime(LocalTime.MAX);
        
        double ganancias = ventaService.calcularGananciasPorPeriodo(inicio, fin);
        return ResponseEntity.ok(ganancias);
    }
    
    @GetMapping("/ganancias/año")
    public ResponseEntity<Double> obtenerGananciasAnuales(
            @RequestParam int año) {
        LocalDate fechaInicio = LocalDate.of(año, 1, 1);
        LocalDate fechaFin = LocalDate.of(año, 12, 31);
        
        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin.atTime(LocalTime.MAX);
        
        double ganancias = ventaService.calcularGananciasPorPeriodo(inicio, fin);
        return ResponseEntity.ok(ganancias);
    }
    
    /**
     * Crea una nueva venta con enlaces HATEOAS
     * 
     * @param ventaDto datos de la venta a crear
     * @return EntityModel con la venta creada y enlaces relacionados
     */
    @PostMapping
    public ResponseEntity<EntityModel<VentaDto>> crearVenta(@RequestBody VentaDto ventaDto) {
        try {
            VentaDto creada = ventaService.crear(ventaDto);
            EntityModel<VentaDto> ventaModel = agregarEnlaces(creada);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(ventaModel);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<VentaDto> actualizarVenta(@PathVariable Integer id, @RequestBody VentaDto ventaDto) {
        try {
            VentaDto actualizada = ventaService.actualizar(id, ventaDto);
            return ResponseEntity.ok(actualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarVenta(@PathVariable Integer id) {
        try {
            ventaService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Endpoints de debug
    @GetMapping("/debug/count")
    public ResponseEntity<Long> contarVentasNativo() {
        Long count = ventaService.contarVentasNativo();
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/debug/raw")
    public ResponseEntity<List<Object[]>> obtenerVentasNativo() {
        List<Object[]> ventas = ventaService.obtenerVentasNativo();
        return ResponseEntity.ok(ventas);
    }
}
