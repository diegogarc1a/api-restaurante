package com.sistema.apirestaurante.repositories;

import com.sistema.apirestaurante.entidades.DetalleVenta;
import com.sistema.apirestaurante.entidades.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {

    List<DetalleVenta> findAllByVenta(Venta venta);
}
