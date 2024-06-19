package com.sistema.apirestaurante.repositories;

import com.sistema.apirestaurante.dtos.VentasDiarias;
import com.sistema.apirestaurante.entidades.EstadoVenta;
import com.sistema.apirestaurante.entidades.Venta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {

    @Query("SELECT v FROM Venta v INNER JOIN DetalleVenta dv ON v.id = dv.venta.id WHERE dv.id = :detalleVentaId")
    Venta findByDetalleVentaId(Long detalleVentaId);


    Page<Venta> findByEstadoOrderByFechaAsc(EstadoVenta estadoVenta, Pageable pageable);

    @Query("SELECT v FROM Venta v WHERE v.estado = :estado")
    Page<Venta> findByEstadoOrderByFecha(EstadoVenta estado, Pageable pageable);

    @Query(value = "SELECT COUNT(DISTINCT v.id) AS cantidadPedidosHoy, " +
            "COALESCE(SUM(CASE WHEN v.estado = 'Pagado' THEN dv.total ELSE 0 END), 0) AS IngresosObtenidos, " +
            "COALESCE(SUM(CASE WHEN v.estado = 'Pagado' THEN dv.cantidad ELSE 0 END), 0) AS ProductosVendidos, " +
            "(SELECT COUNT(DISTINCT id) FROM venta WHERE estado = 'Proceso' AND DATE(fecha) = CURRENT_DATE) AS PedidosEnProceso, " +
            "(SELECT COUNT(DISTINCT id) FROM venta WHERE estado = 'Terminado' AND DATE(fecha) = CURRENT_DATE) AS PedidosPendientesDePago, " +
            "(SELECT COUNT(DISTINCT id) FROM venta WHERE estado = 'Pagado' AND DATE(fecha) = CURRENT_DATE) AS PedidosTerminados " +
            "FROM venta v INNER JOIN detalleventa dv ON dv.venta_id = v.id " +
            "WHERE DATE(v.fecha) = CURRENT_DATE", nativeQuery = true)
    List<Object[]> obtenerEstadisticasHoyNativo();


    @Query(value = "SELECT pr.nombre, SUM(dv.cantidad) AS cantidad " +
            "FROM venta v " +
            "INNER JOIN detalleventa dv ON dv.venta_id = v.id " +
            "INNER JOIN producto pr ON dv.producto_id = pr.id " +
            "WHERE v.estado = 'Pagado' " +
            "GROUP BY pr.id " +
            "ORDER BY cantidad DESC", nativeQuery = true)
    List<Object[]> ventasPorProducto();

    @Query(value = "SELECT " +
            "    TO_CHAR(DATE(v.fecha), 'MM/dd/yyyy') AS fecha, " +
            "    SUM(v.preciototal) AS ingresosTotales, " +
            "    COUNT(v.id) AS cantidadVentas, " +
            "    SUM(dv.cantidad) AS cantidadProductos " +
            "FROM venta v " +
            "INNER JOIN (SELECT venta_id, SUM(cantidad) AS cantidad " +
            "             FROM detalleventa " +
            "             GROUP BY venta_id) dv ON v.id = dv.venta_id " +
            "WHERE v.fecha >= DATE_TRUNC('day', CURRENT_DATE) - INTERVAL '7 days' AND v.estado = 'Pagado'" +
            "GROUP BY DATE(v.fecha) " +
            "ORDER BY fecha ASC", nativeQuery = true)
    List<Object[]> ventasPorSemana();


}
