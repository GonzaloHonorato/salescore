package com.salescore.salescore.core.ventas.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "VENTAS")
public class Venta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "venta_seq")
    @SequenceGenerator(name = "venta_seq", sequenceName = "VENTA_SEQ", allocationSize = 1)
    private Integer id;
    
    @Column(nullable = false)
    private LocalDateTime fecha;
    
    @Column(nullable = false)
    private Double total;
    
    @Column(name = "CLIENTE_NOMBRE", length = 100)
    private String clienteNombre;
    
    @Column(name = "CLIENTE_RUT", length = 12)
    private String clienteRut;
    
    @Column(length = 100)
    private String vendedor;
    
    @OneToMany(mappedBy = "venta", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<DetalleVenta> detalles = new ArrayList<>();
}
