package com.sistema.apirestaurante.repositories;

import com.sistema.apirestaurante.entidades.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {
}
