package com.application.persistence.repository;

import com.application.persistence.entity.compra.Compra;
import com.application.persistence.entity.compra.enums.EEstado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {
    List<Compra> findByEstadoAndFechaBefore(EEstado estado, LocalDateTime fecha);

    @Query("SELECT c FROM Compra c JOIN FETCH c.usuario ORDER BY c.fecha DESC")
    List<Compra> findAllComprasWithUsuarioOrderByFechaDesc();
}