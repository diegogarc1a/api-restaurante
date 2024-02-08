package com.sistema.apirestaurante.repositories;

import com.sistema.apirestaurante.entidades.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    List<Categoria> findByDescripcionContaining(String descripcion);

}
