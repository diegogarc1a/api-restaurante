package com.sistema.apirestaurante.dtos;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class VentaPostDTO {
    private String nombrecliente;
    private LocalDateTime fecha;
    private BigDecimal preciototal;
    private String estado;
    private List<DetalleVentaPostDTO> listaDetalleVenta;

}
