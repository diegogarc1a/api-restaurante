package com.sistema.apirestaurante.services.impl;

import com.sistema.apirestaurante.entidades.Producto;
import com.sistema.apirestaurante.repositories.ProductoRepository;
import com.sistema.apirestaurante.services.ProductoService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }


    @Override
    public Producto guardarProducto(Producto producto) throws Exception {
        return productoRepository.save(producto);
    }

    @Override
    public Producto editarProducto(Producto producto) throws Exception {
        return productoRepository.save(producto);
    }

    @Override
    public List<Producto> listaProductos() throws Exception {
        return productoRepository.findAll();
    }

    @Override
    public Producto buscarPorId(Long id) throws Exception {
        return productoRepository.findById(id).get();
    }

    @Override
    public void eliminar(Long id) throws Exception {
        Producto producto = new Producto();
        producto.setId(id);
        productoRepository.delete(producto);
    }

    @Override
    public void cambiarEstado(Long id) throws Exception {
        Producto producto = new Producto();
        producto = productoRepository.findById(id).get();
        producto.setEstado(!producto.getEstado());
        productoRepository.save(producto);
    }
}
