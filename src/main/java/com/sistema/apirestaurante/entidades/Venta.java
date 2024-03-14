package com.sistema.apirestaurante.entidades;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "venta")
@Data
@ToString(exclude = {"listaDetalleVenta"})
public class Venta implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotBlank
    @Column(name = "nombrecliente", nullable = false, length = 100)
    private String nombrecliente;

    @NotNull
    @Column(name = "fecha", nullable = false)
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime fecha = LocalDateTime.now();


    @NotNull
    @Column(name = "preciototal", nullable = false, precision = 10, scale = 2)
    private BigDecimal preciototal;

    @NotBlank
    @Column(name = "estado", nullable = false, length = 50)
    private String estado;


    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "venta")
    private List<DetalleVenta> listaDetalleVenta = new ArrayList<>();



}
