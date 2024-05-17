package com.sistema.apirestaurante.services.impl;

import com.sistema.apirestaurante.entidades.DetalleVenta;
import com.sistema.apirestaurante.entidades.EstadoVenta;
import com.sistema.apirestaurante.entidades.Producto;
import com.sistema.apirestaurante.entidades.Venta;
import com.sistema.apirestaurante.mappers.MapStructMapper;
import com.sistema.apirestaurante.repositories.DetalleVentaRepository;
import com.sistema.apirestaurante.repositories.ProductoRepository;
import com.sistema.apirestaurante.repositories.VentaRepository;
import com.sistema.apirestaurante.services.VentaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;
    private final DetalleVentaRepository detalleVentaRepository;
    private final ProductoRepository productoRepository;

    private final MapStructMapper mapStructMapper;

    private final SimpMessagingTemplate simpMessagingTemplate;



    public VentaServiceImpl(VentaRepository ventaRepository, DetalleVentaRepository detalleVentaRepository, ProductoRepository productoRepository, MapStructMapper mapStructMapper, SimpMessagingTemplate simpMessagingTemplate) {
        this.ventaRepository = ventaRepository;
        this.detalleVentaRepository = detalleVentaRepository;
        this.productoRepository = productoRepository;
        this.mapStructMapper = mapStructMapper;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }



    @Override
    public Venta guardarVenta(Venta venta, List<DetalleVenta> listaDetalleVenta) throws Exception {
        if(listaDetalleVenta.isEmpty()){
            throw new RuntimeException("No existen productos agregados en esta venta");
        }else{
        Venta ventaGuardar = venta;
        ventaGuardar.setListaDetalleVenta(null);
        LocalDateTime f = LocalDateTime.now();
        ventaGuardar.setFecha(f);
        ventaRepository.save(ventaGuardar);
        for (DetalleVenta dv : listaDetalleVenta){
            dv.setVenta(ventaGuardar);
            Producto producto = productoRepository.findById(dv.getProducto().getId())
                    .orElseThrow( () -> new RuntimeException("Producto no encontrado"));
            dv.setProducto(producto);
            dv.setPrecio(producto.getPrecio());
            BigDecimal total = producto.getPrecio().multiply(BigDecimal.valueOf(dv.getCantidad()));
            dv.setTotal(total);

            detalleVentaRepository.save(dv);
        }

        BigDecimal precioTotalVenta = listaDetalleVenta.stream()
                .map(DetalleVenta::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        ventaGuardar.setPreciototal(precioTotalVenta);

        ventaRepository.save(venta);
        venta.setListaDetalleVenta(listaDetalleVenta);

        //Envia los datos al WS
        simpMessagingTemplate.convertAndSend("/topic/ventas",venta);
        return venta;
        }
    }

    @Override
    public Venta editarVenta(Venta venta, List<DetalleVenta> detalleVentas) throws Exception {
        Venta ventaGuardar = venta;
        ventaGuardar.setListaDetalleVenta(null);
        if(venta.getEstado().equals(EstadoVenta.Terminado)){
        LocalDateTime f = LocalDateTime.now();
        ventaGuardar.setFecha(f);
        }
        ventaGuardar.setEstado(EstadoVenta.Proceso);

        ventaRepository.save(ventaGuardar);

        for (DetalleVenta dv : detalleVentas){
            dv.setVenta(ventaGuardar);

            Producto producto = productoRepository.findById(dv.getProducto().getId())
                    .orElseThrow( () -> new RuntimeException("Producto no encontrado"));

            dv.setProducto(producto);
            dv.setPrecio(producto.getPrecio());
            BigDecimal total = producto.getPrecio().multiply(BigDecimal.valueOf(dv.getCantidad()));
            dv.setTotal(total);

            //Establece el estado final
            if(dv.getEstado().equals(true)){
                dv.setEstadoFinal(true);
            }

            detalleVentaRepository.save(dv);
        }

        BigDecimal precioTotalVenta = detalleVentas.stream()
                .map(DetalleVenta::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        ventaGuardar.setPreciototal(precioTotalVenta);

        ventaRepository.save(venta);
        venta.setListaDetalleVenta(detalleVentas);

        //Envia los datos al WS
        simpMessagingTemplate.convertAndSend("/topic/editventas",venta);
        return venta;
    }

    public Venta finalizarVenta(Long id) throws Exception{
        Venta ventaGuardada = ventaRepository.findById(id).get();
        ventaGuardada.setEstado(EstadoVenta.valueOf("Terminado"));

        for (DetalleVenta dv : ventaGuardada.getListaDetalleVenta()) {
            //Establece el estado final
            if (dv.getEstado().equals(true)) {
                dv.setEstadoFinal(true);
            }
        }

        ventaRepository.save(ventaGuardada);

        //Envia los datos al WS
        simpMessagingTemplate.convertAndSend("/topic/editventas",ventaGuardada);
        return ventaGuardada;
    }

    public Venta pagarVenta(Long id, BigDecimal cantidad) throws Exception{
        Venta ventaGuardada = ventaRepository.findById(id).get();
        if( cantidad.compareTo(ventaGuardada.getPreciototal()) < 0 ){
        throw new RuntimeException("La cantidad dada no es mayor o igual al precio total");
        }
        ventaGuardada.setEstado(EstadoVenta.valueOf("Pagado"));
        ventaRepository.save(ventaGuardada);

        //Envia los datos al WS
        simpMessagingTemplate.convertAndSend("/topic/editventas",ventaGuardada);
        return ventaGuardada;
    }

    public Venta cambiarEstadoDetalleVenta(DetalleVenta detalleVenta) throws Exception {
        DetalleVenta detalleVentaExistente = detalleVentaRepository.findById(detalleVenta.getId()).orElse(null);
        if (detalleVentaExistente != null) {
            // Actualiza el estado
            detalleVentaExistente.setEstado(!detalleVentaExistente.getEstado());
            detalleVentaRepository.save(detalleVentaExistente); // Guarda los cambios
            Venta ventaEditada = ventaRepository.findByDetalleVentaId(detalleVentaExistente.getId());

            if (ventaEditada != null) {
                // Ordenar la lista de detalleVentas por ID en forma ascendente
                ventaEditada.getListaDetalleVenta().sort(Comparator.comparingLong(DetalleVenta::getId).reversed());
            }

            //Envia los datos al WS
            simpMessagingTemplate.convertAndSend("/topic/editventas",ventaEditada);
            return ventaEditada;
        }
        return null;
    }

    @Override
    public List<Venta> listaVentas() throws Exception {
       /* return ventaRepository.findAll();*/
        List<Venta> ventas = ventaRepository.findAll();

        for (Venta venta : ventas) {
            Collections.sort(venta.getListaDetalleVenta(), Comparator.comparing(DetalleVenta::getId).reversed());
        }
        return ventas;
    }

    @Override
    public Venta buscarPorId(Long id) throws Exception {
        return ventaRepository.findById(id).get();
    }

    @Override
    public void eliminar(Long id) throws Exception {
        Venta venta = new Venta();
        venta.setId(id);
        ventaRepository.delete(venta);
    }

    @Override
    public void cambiarEstado(Venta venta) throws Exception {
        Long id = venta.getId();
        Venta ventaGuardar = ventaRepository.findById(id).get();
        ventaGuardar.setEstado(venta.getEstado());
        ventaGuardar.setListaDetalleVenta(null);
        ventaRepository.save(ventaGuardar);
    }


    //METODO CON DTOS
  /*  @Override
    public Venta guardarVenta(VentaPostDTO ventaPostDTO, List<DetalleVentaPostDTO> detalleVentaPostDTOS) throws Exception {

        Venta ventaGuardar = mapStructMapper.ventaPostDtoToVenta(ventaPostDTO);
        ventaGuardar.setListaDetalleVenta(null);
        List<DetalleVenta> listaDetalleVenta = mapStructMapper.mapDetalleVentaDtoToDetalleVenta(detalleVentaPostDTOS);

        ventaRepository.save(ventaGuardar);

        for (DetalleVenta dv : listaDetalleVenta){
            dv.setVenta(ventaGuardar);

            Producto producto = productoRepository.findById(dv.getProducto().getId())
                    .orElseThrow( () -> new RuntimeException("Producto no encontrado"));

            dv.setProducto(producto);
            dv.setPrecio(producto.getPrecio());
            BigDecimal total = producto.getPrecio().multiply(BigDecimal.valueOf(dv.getCantidad()));
            dv.setTotal(total);

            detalleVentaRepository.save(dv);
        }

        BigDecimal precioTotalVenta = listaDetalleVenta.stream()
                .map(DetalleVenta::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        ventaGuardar.setPreciototal(precioTotalVenta);

        return ventaRepository.save(ventaGuardar);
    }*/
}
