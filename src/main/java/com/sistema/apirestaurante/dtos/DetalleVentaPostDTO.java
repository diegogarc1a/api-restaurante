package com.sistema.apirestaurante.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DetalleVentaPostDTO {
    private Integer cantidad;
    private String descripcion;
    private BigDecimal precio;
    private BigDecimal total;
    private VentaPostDTO venta;
    private ProductoPostDTO producto;

}
