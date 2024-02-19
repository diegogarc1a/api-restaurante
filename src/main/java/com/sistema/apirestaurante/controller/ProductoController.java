package com.sistema.apirestaurante.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sistema.apirestaurante.entidades.Producto;
import com.sistema.apirestaurante.repositories.ProductoRepository;
import com.sistema.apirestaurante.services.ProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/productos")
@CrossOrigin("*")
public class ProductoController {

    private final ProductoService productoService;
    private final ProductoRepository productoRepository;

    public ProductoController(ProductoService productoService, ProductoRepository productoRepository) {
        this.productoService = productoService;
        this.productoRepository = productoRepository;
    }

    @PostMapping("/")
    public ResponseEntity<Producto> guardarProducto(@RequestParam String productoJSON, @RequestParam MultipartFile file) throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        Producto producto = objectMapper.readValue(productoJSON, Producto.class);
        return ResponseEntity.ok(productoService.guardarProducto(producto, file));
    }

    @PostMapping("/foto")
    public void guardarFoto(@RequestParam("file") MultipartFile file, @RequestParam("id") Long id) throws Exception{
        productoService.guardarFoto(file,id);
    }

    @PutMapping("/")
    public ResponseEntity<Producto> editarProducto(@RequestBody Producto producto) throws Exception{
        return ResponseEntity.ok(productoService.editarProducto(producto));
    }

    @GetMapping("/{id}")
    public Producto buscarPorId(@PathVariable("id") Long id) throws Exception{
        return productoService.buscarPorId(id);
    }

    @GetMapping("/")
    public ResponseEntity<?> listaProductos() throws Exception{
        return ResponseEntity.ok(productoService.listaProductos());
    }

    @DeleteMapping("/{id}")
    public void eliminarProducto(@PathVariable("id") Long id) throws Exception{
        productoService.eliminar(id);
    }

    @PutMapping("/{id}")
    public void cambiarEstado(@PathVariable("id") Long id) throws Exception{
        productoService.cambiarEstado(id);
    }

}
