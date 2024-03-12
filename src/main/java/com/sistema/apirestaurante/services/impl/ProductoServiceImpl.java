package com.sistema.apirestaurante.services.impl;

import com.sistema.apirestaurante.entidades.Producto;
import com.sistema.apirestaurante.repositories.ProductoRepository;
import com.sistema.apirestaurante.services.ProductoService;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;


@Service
public class ProductoServiceImpl implements ProductoService {

    private static final String BASE_URL = "src/main/resources/static/img";

    private final ProductoRepository productoRepository;
    private final ResourceLoader resourceLoader;

    public ProductoServiceImpl(ProductoRepository productoRepository, ResourceLoader resourceLoader) {
        this.productoRepository = productoRepository;
        this.resourceLoader = resourceLoader;
    }


    @Override
    public Producto guardarProducto(Producto producto, MultipartFile file) throws Exception {
        if(file != null){
        String rutaAbsolta = "C://Restaurante//recursos";
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        String filepath = Paths.get(rutaAbsolta, filename).toString();
        Files.copy(file.getInputStream(), Paths.get(filepath), StandardCopyOption.REPLACE_EXISTING);
        producto.setFoto(filename);
        }
        return productoRepository.save(producto);
    }

    @Override
    public void guardarFoto(MultipartFile file, Long id) throws Exception {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        String filepath = Paths.get(BASE_URL, filename).toString();
        Files.copy(file.getInputStream(), Paths.get(filepath), StandardCopyOption.REPLACE_EXISTING);
        Producto producto = productoRepository.findById(id).get();
        producto.setFoto(filepath);
        productoRepository.save(producto);
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
        if(tieneVentasAsociadas(id)){
            throw new RuntimeException("No se puede eliminar este registro. Tiene registros asociados.");
        }else{
        Producto producto = new Producto();
        producto.setId(id);
        productoRepository.delete(producto);
        }
    }

    @Override
    public void cambiarEstado(Long id) throws Exception {
        Producto producto = productoRepository.findById(id).get();
        producto.setEstado(!producto.getEstado());
        productoRepository.save(producto);
    }

    public boolean tieneVentasAsociadas(Long id) throws Exception {
        Producto producto = productoRepository.findById(id).get();
        if(!producto.getListaDetalleVenta().isEmpty()){
            return true;
        }else{
            return false;
        }
    }

}
