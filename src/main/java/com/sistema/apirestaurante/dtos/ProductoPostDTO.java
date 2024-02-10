package com.sistema.apirestaurante.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductoPostDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private String tipo;
    private String foto;
}
