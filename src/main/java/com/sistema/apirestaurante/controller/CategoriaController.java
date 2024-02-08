package com.sistema.apirestaurante.controller;

import com.sistema.apirestaurante.entidades.Categoria;
import com.sistema.apirestaurante.repositories.CategoriaRepository;
import com.sistema.apirestaurante.services.CategoriaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categorias")
@CrossOrigin("*")
public class CategoriaController {

    private final CategoriaService categoriaService;
    private final CategoriaRepository categoriaRepository;

    public CategoriaController(CategoriaService categoriaService,
                               CategoriaRepository categoriaRepository) {
        this.categoriaService = categoriaService;
        this.categoriaRepository = categoriaRepository;
    }

    @PostMapping("/")
    public ResponseEntity<Categoria> guardarCategoria(@RequestBody Categoria categoria) throws Exception{
        return ResponseEntity.ok(categoriaService.guardarCategoria(categoria));
    }

    @PutMapping("/")
    public ResponseEntity<Categoria> editarCategoria(@RequestBody Categoria categoria) throws Exception{
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

}
