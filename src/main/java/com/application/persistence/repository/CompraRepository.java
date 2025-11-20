package com.application.persistence.repository;

import com.application.persistence.entity.compra.Compra;
import com.application.persistence.entity.compra.enums.EEstado;
import com.application.presentation.dto.grafica.EstadisticasMensualesResponse;
import com.application.presentation.dto.grafica.comprasRecientes.CompraResumenResponse;
import com.application.presentation.dto.grafica.historicoVentas.VentasAnualesDTO;
import com.application.presentation.dto.grafica.ventasCiudades.VentasCiudadDTO;
import com.application.presentation.dto.grafica.ventasTotales.VentasPorEstadoDTO;
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

    @Query(value = """
                SELECT SUM(c.total) 
                FROM compra c
                WHERE c.estado = 'PAGADO'
                  AND YEAR(c.fecha) = YEAR(CURDATE())
            """, nativeQuery = true)
    Double getIngresosAnuales();

    @Query(value = """
                SELECT COUNT(*) 
                FROM compra c
                WHERE c.estado = 'PAGADO'
                  AND YEAR(c.fecha) = YEAR(CURDATE())
            """, nativeQuery = true)
    Long getTotalComprasAnuales();

    @Query("SELECT new com.application.presentation.dto.grafica.EstadisticasMensualesResponse(" +
            "YEAR(c.fecha), MONTH(c.fecha), SUM(c.total), 0.0) " +
            "FROM Compra c " +
            "WHERE c.estado = 'PAGADO' " +
            "GROUP BY YEAR(c.fecha), MONTH(c.fecha) " +
            "ORDER BY YEAR(c.fecha), MONTH(c.fecha)")
    List<EstadisticasMensualesResponse> findIngresosMensuales();

    @Query("""
                SELECT new com.application.presentation.dto.grafica.ventasTotales.VentasPorEstadoDTO(
                    CAST(c.estado AS string),
                    COUNT(c),
                    SUM(c.total)
                )
                FROM Compra c
                WHERE c.fecha >= :fechaInicio
                GROUP BY c.estado
            """)
    List<VentasPorEstadoDTO> findVentasPorEstadoUltimos12Meses(@Param("fechaInicio") LocalDateTime fechaInicio);

    @Query("SELECT new com.application.presentation.dto.grafica.ventasCiudades.VentasCiudadDTO(" +
            "e.ciudad, COUNT(DISTINCT c.compraId), SUM(dv.cantidad), COUNT(DISTINCT e.empresaId)) " +
            "FROM Compra c " +
            "JOIN c.usuario u " +
            "JOIN u.empresa e " +
            "JOIN c.detalleVentas dv " +
            "WHERE c.fecha >= :fechaInicio " +
            "AND c.estado = 'PAGADO' " +
            "AND e.ciudad IS NOT NULL " +
            "AND e.ciudad != '' " +
            "GROUP BY e.ciudad " +
            "ORDER BY SUM(dv.cantidad) DESC")
    List<VentasCiudadDTO> findVentasPorCiudad(@Param("fechaInicio") LocalDateTime fechaInicio);

    @Query("SELECT new com.application.presentation.dto.grafica.historicoVentas.VentasAnualesDTO(" +
            "YEAR(c.fecha), COUNT(c), SUM(c.total), SUM(c.subtotal), SUM(c.iva)) " +
            "FROM Compra c " +
            "WHERE c.estado = 'PAGADO' " +
            "GROUP BY YEAR(c.fecha) " +
            "ORDER BY YEAR(c.fecha) ASC")
    List<VentasAnualesDTO> findVentasAnuales();

    @Query("""
                SELECT COALESCE(SUM(c.total), 0) 
                FROM Compra c 
                WHERE c.estado = 'PAGADO' 
                AND YEAR(c.fecha) = YEAR(CURRENT_DATE) 
                AND MONTH(c.fecha) BETWEEN 1 AND 6
            """)
    Double findIngresosSemestreActual();

    @Query("""
                SELECT COALESCE(SUM(c.total), 0) 
                FROM Compra c WHERE c.estado = 'PAGADO' 
                AND YEAR(c.fecha) = YEAR(CURRENT_DATE) - 1 
                AND MONTH(c.fecha) BETWEEN 7 AND 12
            """)
    Double findIngresosSemestreAnterior();

}