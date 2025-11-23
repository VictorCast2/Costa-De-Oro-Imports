package com.application.persistence.repository;

import com.application.persistence.entity.factura.FacturaProveedor;
import com.application.presentation.dto.grafica.EstadisticasMensualesResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacturaProveedorRepository extends JpaRepository<FacturaProveedor, Long> {

    // Verificar si ya existe una factura con el mismo número
    boolean existsByNumeroFactura(String numeroFactura);

    // Obtener facturas por proveedor
    List<FacturaProveedor> findByUsuarioUsuarioIdOrderByFechaRegistroDesc(Long usuarioId);

    @Query("SELECT new com.application.presentation.dto.grafica.EstadisticasMensualesResponse(" +
            "YEAR(f.fechaEmision), MONTH(f.fechaEmision), 0.0, SUM(f.total)) " +
            "FROM FacturaProveedor f " +
            "WHERE f.activo = true " +
            "GROUP BY YEAR(f.fechaEmision), MONTH(f.fechaEmision) " +
            "ORDER BY YEAR(f.fechaEmision), MONTH(f.fechaEmision)")
    List<EstadisticasMensualesResponse> findGastosMensuales();

    @Query("""
            SELECT COALESCE(SUM(f.total), 0) 
            FROM FacturaProveedor f 
            WHERE f.activo = true 
            AND YEAR(f.fechaEmision) = YEAR(CURRENT_DATE) 
            AND MONTH(f.fechaEmision) BETWEEN 1 AND 6
            """)
    Double findGastosSemestreActual();

    @Query("""
            SELECT COALESCE(SUM(f.total), 0) 
            FROM FacturaProveedor f 
            WHERE f.activo = true 
            AND YEAR(f.fechaEmision) = YEAR(CURRENT_DATE) - 1 
            AND MONTH(f.fechaEmision) BETWEEN 7 AND 12
            """)
    Double findGastosSemestreAnterior();
}
