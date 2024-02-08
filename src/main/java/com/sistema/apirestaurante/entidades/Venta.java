package com.sistema.apirestaurante.entidades;


import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "venta")
@Data
public class Venta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nombrecliente", nullable = false, length = 100)
    private String nombrecliente;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "preciototal", nullable = false, precision = 10, scale = 2)
    private BigDecimal preciototal;

    @Column(name = "estado", nullable = false, length = 50)
    private String estado;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "venta")
    private List<DetalleVenta> listaDetalleVenta = new ArrayList<>();

}
