package com.sistema.apirestaurante.repositories;

import com.sistema.apirestaurante.entidades.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface VentaRepository extends JpaRepository<Venta, Long> {

    @Query("SELECT v FROM Venta v INNER JOIN DetalleVenta dv ON v.id = dv.venta.id WHERE dv.id = :detalleVentaId")
    Venta findByDetalleVentaId(Long detalleVentaId);


}
