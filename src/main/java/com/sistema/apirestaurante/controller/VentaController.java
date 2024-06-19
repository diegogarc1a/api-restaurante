package com.sistema.apirestaurante.controller;

import com.sistema.apirestaurante.dtos.VentaPostDTO;
import com.sistema.apirestaurante.dtos.VentasDiarias;
import com.sistema.apirestaurante.entidades.DetalleVenta;
import com.sistema.apirestaurante.entidades.EstadoVenta;
import com.sistema.apirestaurante.entidades.Venta;
import com.sistema.apirestaurante.repositories.VentaRepository;
import com.sistema.apirestaurante.services.VentaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
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
        /*ventaService.eliminarDetalleVenta(venta, venta.getListaDetalleVenta());*/
        return ResponseEntity.ok(ventaService.editarVenta(venta, venta.getListaDetalleVenta()));
    }

    @PatchMapping("/finalizarVenta")
    public ResponseEntity<?> finalizarVenta(@RequestBody Venta venta ) throws Exception{
        return ResponseEntity.ok(ventaService.finalizarVenta(venta.getId()));
    }

    @PatchMapping("/pagarVenta")
    public ResponseEntity<?> pagarVenta(@RequestParam Long id, @RequestParam BigDecimal cantidad) throws Exception {
        return ResponseEntity.ok(ventaService.pagarVenta(id, cantidad));
    }

    @PatchMapping("/cambiarEstadoDv")
    public ResponseEntity<?> editarDetalleVenta(@RequestBody DetalleVenta dv) throws Exception{
        return ResponseEntity.ok(ventaService.cambiarEstadoDetalleVenta(dv));
    }

    @GetMapping("/")
    public ResponseEntity<?> listaVenta() throws Exception{
        return ResponseEntity.ok(ventaService.listaVentas());
    }

   /* @GetMapping("/lista")
    public ResponseEntity<Page<Venta>> listaVentas(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "5") int size,
                                                   @RequestParam(defaultValue = "Proceso") String estado) throws Exception {
        Pageable pageable = PageRequest.of(page, size);
        Page<Venta> ventas = ventaService.listaVentas(pageable, EstadoVenta.valueOf(estado));
        return ResponseEntity.ok(ventas);
    }
*/

    @GetMapping("/lista")
    public ResponseEntity<Page<Venta>> listaVentas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam(defaultValue = "fecha") String sortBy,
            @RequestParam(defaultValue = "Proceso") String estado) throws Exception {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        if ("desc".equals(sortDirection)) {
            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        }

        Page<Venta> ventas = ventaService.listaVentas(pageable, EstadoVenta.valueOf(estado), sortDirection);

        return ResponseEntity.ok(ventas);
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

    @GetMapping("/estadisticas-hoy")
    public List<VentasDiarias> obtenerEstadisticasHoy() {
        return ventaService.obtenerEstadisticasHoyNativo();
    }

    @GetMapping("/ventas-por-producto")
    public List<Object[]> ventasPorProducto() {
        return ventaService.ventasPorProducto();
    }

    @GetMapping("/ventas-por-semana")
    public List<Object[]> ventasPorSemana() {
        return ventaService.ventasPorSemana();
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
