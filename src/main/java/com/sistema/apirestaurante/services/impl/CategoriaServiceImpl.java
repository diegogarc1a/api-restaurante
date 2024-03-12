package com.sistema.apirestaurante.services.impl;

import com.sistema.apirestaurante.entidades.Categoria;
import com.sistema.apirestaurante.repositories.CategoriaRepository;
import com.sistema.apirestaurante.services.CategoriaService;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaServiceImpl implements CategoriaService {


    private final CategoriaRepository categoriaRepository;

    public CategoriaServiceImpl(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional
    @Override
    public Categoria guardarCategoria(Categoria categoria) throws Exception {
        return categoriaRepository.save(categoria);
    }

    @Transactional
    @Override
    public Categoria editarCategoria(Categoria categoria) throws Exception {
        return categoriaRepository.save(categoria);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Categoria> listaCategorias() throws Exception {
        return categoriaRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Categoria buscarPorId(Long id) throws Exception {
        return categoriaRepository.findById(id).get();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Categoria> categoriasPorDescripcion(String descripcion) throws Exception {
        return categoriaRepository.findByDescripcionContaining(descripcion);
    }

    @Override
    public void eliminar(Long id) throws Exception {
        if(tieneProductosAsociados(id)){
            throw new RuntimeException("No se puede eliminar este registro. Tiene registros asociados.");
        }else{
        Categoria categoria = new Categoria();
        categoria.setId(id);
        categoriaRepository.delete(categoria);

        }
    }

    public boolean tieneProductosAsociados(Long id) throws Exception {
            Categoria categoria = categoriaRepository.findById(id).get();
            if(!categoria.getListaProductos().isEmpty()){
                return true;
            }else{
                return false;
            }
    }
}
