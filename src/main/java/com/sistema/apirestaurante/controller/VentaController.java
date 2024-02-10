package com.sistema.apirestaurante.controller;

import com.sistema.apirestaurante.dtos.VentaPostDTO;
import com.sistema.apirestaurante.entidades.Venta;
import com.sistema.apirestaurante.repositories.VentaRepository;
import com.sistema.apirestaurante.services.VentaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Venta> guardarVenta(@RequestBody Venta venta) throws Exception{
        return ResponseEntity.ok(ventaService.guardarVenta(venta, venta.getListaDetalleVenta()));
    }

    @PutMapping("/")
    public ResponseEntity<Venta> editarVenta(@RequestBody Venta venta) throws Exception{
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



    //PARA USAR CON DTOS
    /*@PostMapping("/")
    public ResponseEntity<Venta> guardarVenta(@RequestBody VentaPostDTO ventaPostDTO) throws Exception{
        return ResponseEntity.ok(ventaService.guardarVenta(ventaPostDTO, ventaPostDTO.getListaDetalleVenta()));
    }*/
}
