package com.sistema.apirestaurante.services;

import com.sistema.apirestaurante.entidades.Categoria;

import java.util.List;

public interface CategoriaService {

    Categoria guardarCategoria(Categoria categoria) throws Exception;

    Categoria editarCategoria(Categoria categoria) throws Exception;

    List<Categoria> listaCategorias() throws Exception;

    Categoria buscarPorId(Long id) throws Exception;

    List<Categoria> categoriasPorDescripcion(String descripcion) throws Exception;

    void eliminar(Long id) throws Exception;

}
