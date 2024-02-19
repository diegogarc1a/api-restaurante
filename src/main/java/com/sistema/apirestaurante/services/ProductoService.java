package com.sistema.apirestaurante.services;

import com.sistema.apirestaurante.entidades.Producto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductoService {

    Producto guardarProducto(Producto producto, MultipartFile file) throws Exception;

    void guardarFoto(MultipartFile file, Long id) throws Exception;
    Producto editarProducto(Producto producto) throws Exception;

    List<Producto> listaProductos() throws Exception;

    Producto buscarPorId(Long id) throws Exception;

    void eliminar(Long id) throws Exception;

    void cambiarEstado(Long id) throws Exception;
}
