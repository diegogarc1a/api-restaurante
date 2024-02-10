package com.sistema.apirestaurante.mappers;


import com.sistema.apirestaurante.dtos.DetalleVentaPostDTO;
import com.sistema.apirestaurante.dtos.ProductoPostDTO;
import com.sistema.apirestaurante.dtos.VentaPostDTO;
import com.sistema.apirestaurante.entidades.DetalleVenta;
import com.sistema.apirestaurante.entidades.Producto;
import com.sistema.apirestaurante.entidades.Venta;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper( componentModel = "spring")
public interface MapStructMapper {
    MapStructMapper INSTANCE = Mappers.getMapper(MapStructMapper.class);

    Venta ventaPostDtoToVenta(VentaPostDTO ventaPostDTO);
    DetalleVenta detalleVentaPostDtoToDetalleVenta(DetalleVentaPostDTO detalleVentaPostDTO);

    List<DetalleVenta> mapDetalleVentaDtoToDetalleVenta(List<DetalleVentaPostDTO> detalleVentasVentaPostDTOS);
    Producto productoPostDTO(ProductoPostDTO productoPostDTO );

}
