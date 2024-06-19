package com.sistema.apirestaurante.services.impl;

import com.sistema.apirestaurante.dtos.VentasDiarias;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
        simpMessagingTemplate.convertAndSend("/topic/dashboard",obtenerEstadisticasHoyNativo());
        return venta;
        }
    }


    @Override
    public Venta editarVenta(Venta venta, List<DetalleVenta> detalleVentas) throws Exception {

        //Elimina los detalleVenta que no son enviados desde el frontend
        eliminarDetalleVenta(venta, venta.getListaDetalleVenta());

        Venta ventaGuardar = venta;
        ventaGuardar.setListaDetalleVenta(null);

        if(venta.getEstado().equals(EstadoVenta.Terminado) && !detalleVentaMatch(venta, detalleVentas)){
            LocalDateTime f = LocalDateTime.now();
            ventaGuardar.setFecha(f);
            ventaGuardar.setEstado(EstadoVenta.Proceso);
        }

        ventaRepository.save(ventaGuardar);

        for (DetalleVenta dv : detalleVentas) {
            dv.setVenta(ventaGuardar);

            Producto producto = productoRepository.findById(dv.getProducto().getId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            dv.setProducto(producto);
            dv.setPrecio(producto.getPrecio());
            BigDecimal total = producto.getPrecio().multiply(BigDecimal.valueOf(dv.getCantidad()));
            dv.setTotal(total);

            // Establece el estado final
            if (dv.getEstado().equals(true)) {
                dv.setEstadoFinal(true);
            }
            detalleVentaRepository.save(dv);
        }


        BigDecimal precioTotalVenta = detalleVentas.stream()
                .map(DetalleVenta::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        ventaGuardar.setPreciototal(precioTotalVenta);


        ventaRepository.save(venta);

        // Ordenar la lista de detalleVentas por ID en forma ascendente
        detalleVentas.sort(Comparator.comparingLong(DetalleVenta::getId).reversed());
        venta.setListaDetalleVenta(detalleVentas);

        // Envia los datos al WS
        simpMessagingTemplate.convertAndSend("/topic/editventas", venta);
        simpMessagingTemplate.convertAndSend("/topic/dashboard",obtenerEstadisticasHoyNativo());

        return venta;
    }

    public void eliminarDetalleVenta(Venta venta, List<DetalleVenta> detalleVentas) throws Exception {
        List<DetalleVenta> detallesVentaActuales = detalleVentaRepository.findAllByVenta(venta);

        detallesVentaActuales.removeIf(dv -> detalleVentas.stream().anyMatch(detalleVenta -> detalleVenta.getId().equals(dv.getId())));

        for (DetalleVenta dv : detallesVentaActuales) {
            detalleVentaRepository.delete(dv);
        }
    }

    public boolean detalleVentaMatch(Venta venta, List<DetalleVenta> detalleVentas) {
        List<DetalleVenta> detallesVentaActuales = detalleVentaRepository.findAllByVenta(venta);

        // Verificamos si los detalles de venta en detalleVentas están presentes en detallesVentaActuales
        for (DetalleVenta detalleVenta : detalleVentas) {
            boolean found = false;
            for (DetalleVenta dv : detallesVentaActuales) {
                if (dv.getId().equals(detalleVenta.getId())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                // Si no encontramos un detalle de venta en detallesVentaActuales, no son iguales
                return false;
            }
        }

        // Verificamos si ambos tienen la misma cantidad de elementos
        return detallesVentaActuales.size() == detalleVentas.size();
    }

    public Venta finalizarVenta(Long id) throws Exception{
        Venta ventaGuardada = ventaRepository.findById(id).get();
        ventaGuardada.setEstado(EstadoVenta.valueOf("Terminado"));

        for (DetalleVenta dv : ventaGuardada.getListaDetalleVenta()) {
            dv.setEstado(true);
            dv.setEstadoFinal(true);
        }

        ventaRepository.save(ventaGuardada);

        //Envia los datos al WS
        simpMessagingTemplate.convertAndSend("/topic/editventas",ventaGuardada);
        simpMessagingTemplate.convertAndSend("/topic/dashboard",obtenerEstadisticasHoyNativo());
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
        simpMessagingTemplate.convertAndSend("/topic/dashboard",obtenerEstadisticasHoyNativo());
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

    /*public Page<Venta> listaVentas(Pageable pageable, EstadoVenta estado) throws Exception{
        Page<Venta> ventas = ventaRepository.findByEstadoOrderByFechaAsc(estado ,pageable);
        for (Venta venta : ventas) {
            Collections.sort(venta.getListaDetalleVenta(), Comparator.comparing(DetalleVenta::getId).reversed());
        }
        return ventas;
    }*/

    public Page<Venta> listaVentas(Pageable pageable, EstadoVenta estado, String sortDirection) throws Exception {
        Sort sort = Sort.by("fecha").ascending();
        if ("desc".equals(sortDirection)) {
            sort = Sort.by("fecha").descending();
        }
        Pageable pageableWithSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Page<Venta> ventas = ventaRepository.findByEstadoOrderByFecha(estado, pageableWithSort);
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
        Venta ventaEliminada = ventaRepository.findById(id).orElseThrow();
        List<DetalleVenta> detalles = ventaEliminada.getListaDetalleVenta();
        detalles.forEach(detalleVenta -> detalleVentaRepository.delete(detalleVenta));
        ventaRepository.delete(ventaEliminada);
        simpMessagingTemplate.convertAndSend("/topic/eliminarventas",ventaEliminada);
        simpMessagingTemplate.convertAndSend("/topic/dashboard",obtenerEstadisticasHoyNativo());
    }

    @Override
    public void cambiarEstado(Venta venta) throws Exception {
        Long id = venta.getId();
        Venta ventaGuardar = ventaRepository.findById(id).get();
        ventaGuardar.setEstado(venta.getEstado());
        ventaGuardar.setListaDetalleVenta(null);
        ventaRepository.save(ventaGuardar);
    }


    @Override
    public List<VentasDiarias> obtenerEstadisticasHoyNativo() {
        List<Object[]> resultados = ventaRepository.obtenerEstadisticasHoyNativo();
        List<VentasDiarias> estadisticas = new ArrayList<>();

        for (Object[] resultado : resultados) {
            VentasDiarias ventasDiarias = new VentasDiarias(
                    ((Long) resultado[0]).intValue(),
                    ((BigDecimal) resultado[1]).doubleValue(), // Convertir BigDecimal a Double
                    ((Long) resultado[2]).intValue(),
                    ((Long) resultado[3]).intValue(),
                    ((Long) resultado[4]).intValue(),
                    ((Long) resultado[5]).intValue()
            );
            estadisticas.add(ventasDiarias);
        }

        return estadisticas;
    }

    @Override
    public List<Object[]> ventasPorProducto() {
        return ventaRepository.ventasPorProducto();
    }

    @Override
    public List<Object[]> ventasPorSemana() {
        return ventaRepository.ventasPorSemana();
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
