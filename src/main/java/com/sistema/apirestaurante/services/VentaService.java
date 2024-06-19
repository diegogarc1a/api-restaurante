package com.sistema.apirestaurante.services;

import com.sistema.apirestaurante.dtos.DetalleVentaPostDTO;
import com.sistema.apirestaurante.dtos.VentaPostDTO;
import com.sistema.apirestaurante.dtos.VentasDiarias;
import com.sistema.apirestaurante.entidades.DetalleVenta;
import com.sistema.apirestaurante.entidades.EstadoVenta;
import com.sistema.apirestaurante.entidades.Venta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface VentaService {

    //PARA USAR CON DTOS
    /*Venta guardarVenta(VentaPostDTO ventaPostDTO, List<DetalleVentaPostDTO> detalleVentaPostDTOS) throws Exception;*/

    Venta guardarVenta(Venta venta, List<DetalleVenta> detalleVentas) throws Exception;

    Venta editarVenta(Venta venta, List<DetalleVenta> detalleVentas) throws Exception;

    void eliminarDetalleVenta(Venta venta, List<DetalleVenta> detalleVentas) throws Exception;

    Venta finalizarVenta(Long id) throws Exception;

    Venta pagarVenta(Long id, BigDecimal cantidad) throws Exception;

    Venta cambiarEstadoDetalleVenta(DetalleVenta detalleVenta) throws Exception;

    List<Venta> listaVentas() throws Exception;

 /*   Page<Venta> listaVentas(Pageable pageable, EstadoVenta estado) throws Exception;*/

    Page<Venta> listaVentas(Pageable pageable, EstadoVenta estado, String sortDirection)  throws Exception;

    Venta buscarPorId(Long id) throws Exception;

    void eliminar(Long id) throws Exception;

    void cambiarEstado(Venta venta) throws Exception;


    List<VentasDiarias> obtenerEstadisticasHoyNativo();

    List<Object[]> ventasPorProducto();

    List<Object[]> ventasPorSemana();
}
