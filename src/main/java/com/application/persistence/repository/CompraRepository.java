package com.application.persistence.repository;

import com.application.persistence.entity.compra.Compra;
import com.application.persistence.entity.compra.enums.EEstado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {
    List<Compra> findByEstadoAndFechaBefore(EEstado estado, LocalDateTime fecha);

    @Query("SELECT c FROM Compra c JOIN FETCH c.usuario ORDER BY c.fecha DESC")
    List<Compra> findAllComprasWithUsuarioOrderByFechaDesc();

    @Query("SELECT c FROM Compra c " +
            "JOIN FETCH c.usuario u " +
            "JOIN FETCH c.detalleVentas dv " +
            "JOIN FETCH dv.producto p " +
            "WHERE c.compraId = :id")
    Optional<Compra> findCompraWithDetalles(@Param("id") Long id);
}