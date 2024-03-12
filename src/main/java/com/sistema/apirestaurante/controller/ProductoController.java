package com.sistema.apirestaurante.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sistema.apirestaurante.entidades.Producto;
import com.sistema.apirestaurante.exeptions.ErrorResponse;
import com.sistema.apirestaurante.repositories.ProductoRepository;
import com.sistema.apirestaurante.services.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    private final ProductoService productoService;
    private final ProductoRepository productoRepository;

    public ProductoController(ProductoService productoService, ProductoRepository productoRepository) {
        this.productoService = productoService;
        this.productoRepository = productoRepository;
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @PostMapping("/")
    public ResponseEntity<?> guardarProducto(@Valid @RequestPart("producto") Producto producto, BindingResult result, @RequestPart(value = "file", required = false) MultipartFile file) throws Exception{
        if( result.hasFieldErrors() ){
            return validation(result);
        }
        return ResponseEntity.ok(productoService.guardarProducto(producto, file));
    }

    @PostMapping("/foto")
    public void guardarFoto(@RequestParam("file") MultipartFile file, @RequestParam("id") Long id) throws Exception{
        productoService.guardarFoto(file,id);
    }

    /*@PutMapping("/")
    public ResponseEntity<?> editarProducto(@Valid @RequestBody Producto producto, BindingResult result) throws Exception{
        if( result.hasFieldErrors() ){
            return validation(result);
        }
        return ResponseEntity.ok(productoService.editarProducto(producto));
    }
*/

    @PutMapping("/")
    public ResponseEntity<?> editarProducto(@Valid @RequestPart("producto") Producto producto, BindingResult result, @RequestPart(value = "file", required = false) MultipartFile file) throws Exception{
        if( result.hasFieldErrors() ){
            return validation(result);
        }
        if(file == null){

        }
        return ResponseEntity.ok(productoService.guardarProducto(producto, file));
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

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach( err -> {
            errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        } );
        return ResponseEntity.badRequest().body(errors);
    }
}
