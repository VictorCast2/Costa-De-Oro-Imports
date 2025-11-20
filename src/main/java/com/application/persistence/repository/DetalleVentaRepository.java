package com.application.persistence.repository;

import com.application.persistence.entity.compra.DetalleVenta;
import com.application.presentation.dto.grafica.columnasApiladas.VentasCategoriaDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {

    @Query("SELECT new com.application.presentation.dto.grafica.columnasApiladas.VentasCategoriaDTO(" +
            "c.nombre, SUM(dv.cantidad)) " +
            "FROM DetalleVenta dv " +
            "JOIN dv.compra co " +
            "JOIN dv.producto p " +
            "JOIN p.categoria c " +
            "WHERE co.fecha >= :fechaInicio " +
            "AND co.estado = 'PAGADO' " +
            "GROUP BY c.categoriaId, c.nombre " +
            "ORDER BY SUM(dv.cantidad) DESC")
    List<VentasCategoriaDTO> findVentasPorCategoriaUltimosByMeses(@Param("fechaInicio") LocalDateTime fechaInicio);
}