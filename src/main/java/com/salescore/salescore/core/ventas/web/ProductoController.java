package com.salescore.salescore.core.ventas.web;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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

import com.salescore.salescore.core.ventas.dto.ProductoDto;
import com.salescore.salescore.core.ventas.service.ProductoService;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    
    @Autowired
    private ProductoService productoService;
    
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<ProductoDto>>> listarTodos() {
        List<EntityModel<ProductoDto>> productos = productoService.listarTodos().stream()
                .map(producto -> EntityModel.of(producto,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProductoController.class).buscarPorId(producto.getId())).withSelfRel(),
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProductoController.class).listarTodos()).withRel("productos")))
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(
                CollectionModel.of(productos,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProductoController.class).listarTodos()).withSelfRel()));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ProductoDto>> buscarPorId(@PathVariable Integer id) {
        return productoService.buscarPorId(id)
                .map(producto -> EntityModel.of(producto, 
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProductoController.class).buscarPorId(id)).withSelfRel(),
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProductoController.class).listarTodos()).withRel("productos"),
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProductoController.class).buscarPorCategoria(producto.getCategoria())).withRel("misma-categoria")))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<CollectionModel<EntityModel<ProductoDto>>> buscarPorCategoria(@PathVariable String categoria) {
        List<EntityModel<ProductoDto>> productos = productoService.buscarPorCategoria(categoria).stream()
                .map(producto -> EntityModel.of(producto,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProductoController.class).buscarPorId(producto.getId())).withSelfRel(),
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProductoController.class).listarTodos()).withRel("productos")))
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(
                CollectionModel.of(productos,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProductoController.class).buscarPorCategoria(categoria)).withSelfRel(),
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProductoController.class).listarTodos()).withRel("todos-productos")));
    }
    
    @GetMapping("/buscar")
    public ResponseEntity<List<ProductoDto>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(productoService.buscarPorNombre(nombre));
    }
    
    @PostMapping
    public ResponseEntity<ProductoDto> crear(@RequestBody ProductoDto productoDto) {
        try {
            ProductoDto creado = productoService.crear(productoDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ProductoDto> actualizar(@PathVariable Integer id, @RequestBody ProductoDto productoDto) {
        try {
            ProductoDto actualizado = productoService.actualizar(id, productoDto);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        try {
            productoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
