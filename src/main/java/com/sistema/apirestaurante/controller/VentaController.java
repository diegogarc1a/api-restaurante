package com.sistema.apirestaurante.controller;

import com.sistema.apirestaurante.dtos.VentaPostDTO;
import com.sistema.apirestaurante.entidades.Venta;
import com.sistema.apirestaurante.repositories.VentaRepository;
import com.sistema.apirestaurante.services.VentaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/ventas")
@CrossOrigin("*")
public class VentaController {

    private final VentaService ventaService;

    private final VentaRepository ventaRepository;

    public VentaController(VentaService ventaService, VentaRepository ventaRepository) {
        this.ventaService = ventaService;
        this.ventaRepository = ventaRepository;
    }

    @PostMapping("/")
    public ResponseEntity<?> guardarVenta(@Valid @RequestBody Venta venta, BindingResult result) throws Exception{
        if( result.hasFieldErrors() ){
            return validation(result);
        }

        return ResponseEntity.ok(ventaService.guardarVenta(venta, venta.getListaDetalleVenta()));
    }

    @PutMapping("/")
    public ResponseEntity<?> editarVenta(@Valid @RequestBody Venta venta, BindingResult result) throws Exception{
        if( result.hasFieldErrors() ){
            return validation(result);
        }
        return ResponseEntity.ok(ventaService.editarVenta(venta, venta.getListaDetalleVenta()));
    }

    @GetMapping("/")
    public ResponseEntity<?> listaVenta() throws Exception{
        return ResponseEntity.ok(ventaService.listaVentas());
    }

    @GetMapping("/{id}")
    public Venta buscarPorId(@PathVariable("id") Long id ) throws Exception{
        return ventaService.buscarPorId(id);
    }

    @DeleteMapping("/{id}")
    public void eliminarVenta(@PathVariable("id") Long id)throws Exception{
        ventaService.eliminar(id);
    }

    @PutMapping("/estado")
    public void cambiarEstado(@RequestBody Venta venta)throws Exception{
        ventaService.cambiarEstado(venta);
    }

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach( err -> {
            errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        } );
        return ResponseEntity.badRequest().body(errors);
    }



    //PARA USAR CON DTOS
    /*@PostMapping("/")
    public ResponseEntity<Venta> guardarVenta(@RequestBody VentaPostDTO ventaPostDTO) throws Exception{
        return ResponseEntity.ok(ventaService.guardarVenta(ventaPostDTO, ventaPostDTO.getListaDetalleVenta()));
    }*/
}
