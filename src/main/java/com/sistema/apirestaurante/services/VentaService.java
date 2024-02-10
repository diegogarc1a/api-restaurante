package com.sistema.apirestaurante.services;

import com.sistema.apirestaurante.dtos.DetalleVentaPostDTO;
import com.sistema.apirestaurante.dtos.VentaPostDTO;
import com.sistema.apirestaurante.entidades.DetalleVenta;
import com.sistema.apirestaurante.entidades.Venta;

import java.util.List;

public interface VentaService {

    //PARA USAR CON DTOS
    /*Venta guardarVenta(VentaPostDTO ventaPostDTO, List<DetalleVentaPostDTO> detalleVentaPostDTOS) throws Exception;*/

    Venta guardarVenta(Venta venta, List<DetalleVenta> detalleVentas) throws Exception;

    Venta editarVenta(Venta venta, List<DetalleVenta> detalleVentas) throws Exception;

    List<Venta> listaVentas() throws Exception;

    Venta buscarPorId(Long id) throws Exception;

    void eliminar(Long id) throws Exception;

    void cambiarEstado(Venta venta) throws Exception;
}
