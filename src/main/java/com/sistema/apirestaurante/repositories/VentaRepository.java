package com.sistema.apirestaurante.repositories;

import com.sistema.apirestaurante.entidades.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VentaRepository extends JpaRepository<Venta, Long> {
}
