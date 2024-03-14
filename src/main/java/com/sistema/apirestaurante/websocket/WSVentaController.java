package com.sistema.apirestaurante.websocket;

import com.sistema.apirestaurante.entidades.Venta;
import com.sistema.apirestaurante.exeptions.ValidationException;
import com.sistema.apirestaurante.services.VentaService;
import jakarta.validation.Valid;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.stereotype.Controller;

@Controller
public class WSVentaController {

    private final VentaService ventaService;

    public WSVentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }


    @MessageMapping("/guardarVenta")
    @SendTo("/topic/ventas")
    public Venta guardarVenta(@Valid @Payload Venta venta) throws Exception {
        try {
            System.out.println("Guardando venta: " + venta);
            ventaService.guardarVenta(venta, venta.getListaDetalleVenta());
            return venta;
        } catch (MethodArgumentNotValidException ex) {
            throw new ValidationException("La venta no es válida", ex);
        }
    }

}
