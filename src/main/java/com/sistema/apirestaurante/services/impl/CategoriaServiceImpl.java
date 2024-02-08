package com.sistema.apirestaurante.services.impl;

import com.sistema.apirestaurante.entidades.Categoria;
import com.sistema.apirestaurante.repositories.CategoriaRepository;
import com.sistema.apirestaurante.services.CategoriaService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;


    public CategoriaServiceImpl(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public Categoria guardarCategoria(Categoria categoria) throws Exception {
        return categoriaRepository.save(categoria);
    }

    @Override
    public Categoria editarCategoria(Categoria categoria) throws Exception {
        return categoriaRepository.save(categoria);
    }

    @Override
    public List<Categoria> listaCategorias() throws Exception {
        return categoriaRepository.findAll();
    }

    @Override
    public Categoria buscarPorId(Long id) throws Exception {
        return categoriaRepository.findById(id).get();
    }

    @Override
    public List<Categoria> categoriasPorDescripcion(String descripcion) throws Exception {
        return categoriaRepository.findByDescripcionContaining(descripcion);
    }

    @Override
    public void eliminar(Long id) throws Exception {
        Categoria categoria = new Categoria();
        categoria.setId(id);
        categoriaRepository.delete(categoria);
    }
}
