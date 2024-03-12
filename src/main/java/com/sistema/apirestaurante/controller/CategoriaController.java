package com.sistema.apirestaurante.controller;

import com.sistema.apirestaurante.entidades.Categoria;
import com.sistema.apirestaurante.exeptions.ErrorResponse;
import com.sistema.apirestaurante.services.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/categorias")
@CrossOrigin(origins = "*")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @PostMapping("/")
    public ResponseEntity<?> guardarCategoria(@Valid @RequestBody Categoria categoria, BindingResult result) throws Exception{
        if( result.hasFieldErrors() ){
            return validation(result);
        }
        return ResponseEntity.ok(categoriaService.guardarCategoria(categoria));
    }

    @PutMapping("/")
    public ResponseEntity<?> editarCategoria(@Valid @RequestBody Categoria categoria, BindingResult result) throws Exception{
        if( result.hasFieldErrors() ){
            return validation(result);
        }
        return ResponseEntity.ok(categoriaService.editarCategoria(categoria));
    }


    @GetMapping("/{id}")
    public Categoria buscarPorId(@PathVariable("id") Long id) throws Exception{
        return categoriaService.buscarPorId(id);
    }

    @GetMapping("/")
    public ResponseEntity<?> listaCategorias() throws Exception{
        return ResponseEntity.ok(categoriaService.listaCategorias());
    }

    @DeleteMapping("/{id}")
    public void eliminarCategoria(@PathVariable("id") Long id) throws Exception{
        categoriaService.eliminar(id);
    }


    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach( err -> {
            errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        } );
        return ResponseEntity.badRequest().body(errors);
    }

}
