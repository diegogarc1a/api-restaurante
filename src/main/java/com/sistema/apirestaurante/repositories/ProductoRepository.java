package com.sistema.apirestaurante.repositories;

import com.sistema.apirestaurante.entidades.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

}
