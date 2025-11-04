package com.application.persistence.repository;

import com.application.persistence.entity.producto.Producto;
import com.application.persistence.entity.producto.enums.ETipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByCategoria_CategoriaId(Long categoriaId);

    @Query("""
            SELECT p
              FROM Producto p
              LEFT JOIN p.detalleVentas dv
             WHERE p.activo = true
             GROUP BY p
             ORDER BY COALESCE(SUM(dv.cantidad), 0) DESC
            """)
    List<Producto> findProductosActivos();

    @Query("""
            SELECT p
              FROM Producto p
              LEFT JOIN p.detalleVentas dv
             WHERE p.activo = true
               AND p.eTipo <> 'UNIDAD'
             GROUP BY p
             ORDER BY COALESCE(SUM(dv.cantidad),0) DESC
            """)
    List<Producto> findProductosActivosMasVendidosNoUnidad();

    @Query("""
            SELECT p
              FROM Producto p
              JOIN p.detalleVentas dv
             WHERE p.activo = true
             GROUP BY p
             ORDER BY SUM(dv.cantidad) DESC
            """)
    List<Producto> findProductosMasVendidosActivos();

    @Query("""
                SELECT p
                  FROM Producto p
                  JOIN p.detalleVentas dv
                 WHERE p.categoria.categoriaId = :categoriaId
                   AND p.activo = true
                 GROUP BY p
                 ORDER BY SUM(dv.cantidad) DESC
            """)
    List<Producto> findProductosMasVendidosByCategoriaIdActivos(@Param("categoriaId") Long categoriaId);

    @Query("""
                SELECT p
                  FROM Producto p
                  JOIN p.detalleVentas dv
                 WHERE p.categoria.categoriaId IN :categoriaIds
                   AND p.activo = true
                 GROUP BY p
                 ORDER BY SUM(dv.cantidad) DESC
            """)
    List<Producto> findProductosMasVendidosByCategoriaIdsActivos(@Param("categoriaIds") List<Long> categoriaIds);

    @Query("SELECT p.stock FROM Producto p WHERE p.productoId = :productoId")
    Integer findStockByProductoId(@Param("productoId") Long productoId);
}