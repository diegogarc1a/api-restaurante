package com.sistema.apirestaurante.repositories;

import com.sistema.apirestaurante.entidades.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {

}
