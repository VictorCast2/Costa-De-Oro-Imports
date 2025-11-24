package com.application.persistence.repository;

import com.application.persistence.entity.producto.Producto;
import com.application.presentation.dto.reporte.CuentasPorPagar.projection.CuentasPagarProjection;
import com.application.presentation.dto.reporte.RentabilidadPorProducto.projection.RentabilidadProductoProjection;
import com.application.presentation.dto.reporte.ReporteCompra.projection.ReporteCompraProjection;
import com.application.presentation.dto.reporte.VentasPorClienteProducto.projection.VentasClienteProductoProjection;
import com.application.presentation.dto.reporte.ventasPorCliente.projection.VentasClienteProjection;
import com.application.presentation.dto.reporte.ventasPorProducto.projection.VentasProductoProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportesNativeRepository extends JpaRepository<Producto, Long> {

    // Ventas por Clientes
    @Query(value = """
                SELECT 
                    COALESCE(e.nit, u.numero_identificacion) as identificacion,
                    CONCAT(u.nombres, ' ', COALESCE(u.apellidos, '')) as cliente,
                    COUNT(DISTINCT c.compra_id) as numeroComprobantes,
                    SUM(c.subtotal) as valorBruto,
                    SUM(c.iva) as impuestoCargo,
                    SUM(c.total) as total
                FROM compra c
                INNER JOIN usuario u ON c.usuario_id = u.usuario_id
                LEFT JOIN empresa e ON u.empresa_id = e.empresa_id
                WHERE YEAR(c.fecha) = :anio
                  AND c.estado = 'PAGADO'
                GROUP BY u.usuario_id, e.nit, u.numero_identificacion, u.nombres, u.apellidos
                ORDER BY total DESC
            """, nativeQuery = true)
    List<VentasClienteProjection> findVentasPorClientes(@Param("anio") int anio);

    @Query(value = """
                SELECT 
                    SUM(c.subtotal) as valorBruto,
                    SUM(c.iva) as impuestoCargo,
                    SUM(c.total) as total
                FROM compra c
                WHERE YEAR(c.fecha) = :anio
                  AND c.estado = 'PAGADO'
            """, nativeQuery = true)
    Object[] findTotalesGeneralesVentas(@Param("anio") int anio);

    // Ventas por Producto
    @Query(value = """
                SELECT 
                    p.codigo_producto as codigoProducto,
                    p.nombre as nombreProducto,
                    COALESCE(e.razon_social, 'Sin proveedor') as referenciaFabrica,
                    SUM(dv.cantidad) as cantidadVendida,
                    SUM(dv.subtotal) as valorBruto,
                    SUM(dv.subtotal * 0.19) as impuestoCargo,
                    SUM(dv.subtotal * 1.19) as total
                FROM detalle_venta dv
                INNER JOIN producto p ON dv.producto_id = p.producto_id
                INNER JOIN compra c ON dv.compra_id = c.compra_id
                LEFT JOIN usuario u_proveedor ON p.usuario_id = u_proveedor.usuario_id
                LEFT JOIN empresa e ON u_proveedor.empresa_id = e.empresa_id
                WHERE YEAR(c.fecha) = :anio
                  AND c.estado = 'PAGADO'
                GROUP BY p.producto_id, p.codigo_producto, p.nombre, e.razon_social
                ORDER BY total DESC
            """, nativeQuery = true)
    List<VentasProductoProjection> findVentasPorProducto(@Param("anio") int anio);

    @Query(value = """
                SELECT 
                    SUM(dv.subtotal) as valorBruto,
                    SUM(dv.subtotal * 0.19) as impuestoCargo,
                    SUM(dv.subtotal * 1.19) as total
                FROM detalle_venta dv
                INNER JOIN compra c ON dv.compra_id = c.compra_id
                WHERE YEAR(c.fecha) = :anio
                  AND c.estado = 'PAGADO'
            """, nativeQuery = true)
    Object[] findTotalesGeneralesVentasProducto(@Param("anio") int anio);

    // Ventas por Cliente por Producto
    @Query(value = """
                SELECT 
                    COALESCE(e_cliente.nit, u_cliente.numero_identificacion) as identificacion,
                    COALESCE(e_cliente.razon_social, CONCAT(u_cliente.nombres, ' ', COALESCE(u_cliente.apellidos, ''))) as cliente,
                    p.codigo_producto as codigoProducto,
                    p.nombre as nombreProducto,
                    COALESCE(e_proveedor.razon_social, 'Sin proveedor') as referenciaFabrica,
                    SUM(dv.cantidad) as cantidadVendida,
                    SUM(dv.subtotal) as valorBruto,
                    SUM(dv.subtotal * 0.19) as impuestoCargo,
                    SUM(dv.subtotal * 1.19) as total
                FROM detalle_venta dv
                INNER JOIN producto p ON dv.producto_id = p.producto_id
                INNER JOIN compra c ON dv.compra_id = c.compra_id
                INNER JOIN usuario u_cliente ON c.usuario_id = u_cliente.usuario_id
                LEFT JOIN empresa e_cliente ON u_cliente.empresa_id = e_cliente.empresa_id
                LEFT JOIN usuario u_proveedor ON p.usuario_id = u_proveedor.usuario_id
                LEFT JOIN empresa e_proveedor ON u_proveedor.empresa_id = e_proveedor.empresa_id
                WHERE YEAR(c.fecha) = :anio
                  AND c.estado = 'PAGADO'
                GROUP BY u_cliente.usuario_id, e_cliente.nit, u_cliente.numero_identificacion, 
                         e_cliente.razon_social, u_cliente.nombres, u_cliente.apellidos,
                         p.producto_id, p.codigo_producto, p.nombre, e_proveedor.razon_social
                ORDER BY cliente, nombreProducto
            """, nativeQuery = true)
    List<VentasClienteProductoProjection> findVentasPorClienteProducto(@Param("anio") int anio);

    @Query(value = """
                SELECT 
                    SUM(dv.subtotal) as valorBruto,
                    SUM(dv.subtotal * 0.19) as impuestoCargo,
                    SUM(dv.subtotal * 1.19) as total
                FROM detalle_venta dv
                INNER JOIN compra c ON dv.compra_id = c.compra_id
                WHERE YEAR(c.fecha) = :anio
                  AND c.estado = 'PAGADO'
            """, nativeQuery = true)
    Object[] findTotalesGeneralesVentasClienteProducto(@Param("anio") int anio);

    // Rentabilidad por Producto
    @Query(value = """
                SELECT 
                    p.tipo as tipo,
                    p.codigo_producto as codigoProducto,
                    p.nombre as nombreProducto,
                    COALESCE(e_proveedor.razon_social, 'Sin proveedor') as referenciaFabrica,
                    c.nombre as categoria,
                    SUM(dv.cantidad) as cantidadVendida,
                    SUM(dv.subtotal) as ventasBrutasTotales,
                    COALESCE(SUM(df.precio_compra * df.cantidad), 0) as costosTotales,
                    (SUM(dv.subtotal) - COALESCE(SUM(df.precio_compra * df.cantidad), 0)) as utilidadPesos,
                    CASE 
                        WHEN SUM(dv.subtotal) > 0 THEN 
                            ((SUM(dv.subtotal) - COALESCE(SUM(df.precio_compra * df.cantidad), 0)) / SUM(dv.subtotal)) * 100 
                        ELSE 0 
                    END as porcentajeUtilidad,
                    CASE 
                        WHEN COALESCE(SUM(df.precio_compra * df.cantidad), 0) > 0 THEN 
                            ((SUM(dv.subtotal) - COALESCE(SUM(df.precio_compra * df.cantidad), 0)) / COALESCE(SUM(df.precio_compra * df.cantidad), 1)) * 100 
                        ELSE 0 
                    END as porcentajeRentabilidad
                FROM detalle_venta dv
                INNER JOIN producto p ON dv.producto_id = p.producto_id
                INNER JOIN compra comp ON dv.compra_id = comp.compra_id
                INNER JOIN categoria c ON p.categoria_id = c.categoria_id
                LEFT JOIN usuario u_proveedor ON p.usuario_id = u_proveedor.usuario_id
                LEFT JOIN empresa e_proveedor ON u_proveedor.empresa_id = e_proveedor.empresa_id
                LEFT JOIN detalle_factura df ON p.producto_id = df.producto_id
                LEFT JOIN factura_proveedor fp ON df.factura_id = fp.factura_id
                WHERE YEAR(comp.fecha) = :anio
                  AND comp.estado = 'PAGADO'
                GROUP BY p.producto_id, p.tipo, p.codigo_producto, p.nombre, 
                         e_proveedor.razon_social, c.nombre
                ORDER BY utilidadPesos DESC
            """, nativeQuery = true)
    List<RentabilidadProductoProjection> findRentabilidadPorProducto(@Param("anio") int anio);

    @Query(value = """
                SELECT 
                    SUM(dv.subtotal) as ventasBrutasTotales,
                    COALESCE(SUM(df.precio_compra * df.cantidad), 0) as costosTotales
                FROM detalle_venta dv
                INNER JOIN compra comp ON dv.compra_id = comp.compra_id
                LEFT JOIN detalle_factura df ON dv.producto_id = df.producto_id
                LEFT JOIN factura_proveedor fp ON df.factura_id = fp.factura_id
                WHERE YEAR(comp.fecha) = :anio
                  AND comp.estado = 'PAGADO'
            """, nativeQuery = true)
    Object[] findTotalesGeneralesRentabilidad(@Param("anio") int anio);

    // Reporte de compras por cliente
    @Query(value = """
                SELECT 
                    COALESCE(e.nit, u.numero_identificacion) as identificacion,
                    COALESCE(e.razon_social, CONCAT(u.nombres, ' ', COALESCE(u.apellidos, ''))) as cliente,
                    COALESCE(e.ciudad, 'Sin ciudad') as ciudad,
                    COALESCE(e.direccion, u.direccion, 'Sin dirección') as sucursal,
                    COUNT(DISTINCT c.compra_id) as totalCompras,
                    SUM(c.subtotal) as totalSubtotal,
                    SUM(c.subtotal * 0.19) as totalIva,
                    SUM(c.subtotal * 1.19) as totalComprado,
                    AVG(c.total) as promedioCompra,
                    MAX(c.fecha) as ultimaCompra,
                    c.metodo_pago as metodoPagoFrecuente
                FROM compra c
                INNER JOIN usuario u ON c.usuario_id = u.usuario_id
                LEFT JOIN empresa e ON u.empresa_id = e.empresa_id
                WHERE YEAR(c.fecha) = :anio
                  AND c.estado = 'PAGADO'
                GROUP BY u.usuario_id, e.nit, u.numero_identificacion, e.razon_social, 
                         u.nombres, u.apellidos, e.ciudad, e.direccion, u.direccion, c.metodo_pago
                ORDER BY totalComprado DESC
            """, nativeQuery = true)
    List<ReporteCompraProjection> findReporteCompras(@Param("anio") int anio);

    @Query(value = """
                SELECT 
                    COUNT(DISTINCT c.compra_id) as totalCompras,
                    COUNT(DISTINCT c.usuario_id) as totalClientes,
                    SUM(c.subtotal) as totalSubtotal,
                    SUM(c.subtotal * 0.19) as totalIva,
                    SUM(c.subtotal * 1.19) as totalComprado,
                    AVG(c.total) as promedioCompra
                FROM compra c
                WHERE YEAR(c.fecha) = :anio
                  AND c.estado = 'PAGADO'
            """, nativeQuery = true)
    Object[] findTotalesGeneralesCompras(@Param("anio") int anio);

    // Proveedores Activos (Cuentas por Pagar)
    @Query(value = """
                SELECT 
                    COALESCE(e.nit, u.numero_identificacion) as identificacion,
                    COALESCE(e.razon_social, CONCAT(u.nombres, ' ', COALESCE(u.apellidos, ''))) as proveedor,
                    COALESCE(e.ciudad, 'Sin ciudad') as ciudad,
                    COALESCE(e.direccion, u.direccion, 'Sin dirección') as sucursal,
                    COUNT(DISTINCT p.producto_id) as productosSuministrados,
                    COUNT(DISTINCT fp.factura_id) as facturasRegistradas,
                    COALESCE(SUM(fp.total), 0) as totalFacturado,
                    COUNT(DISTINCT comp.compra_id) as comprasRealizadas
                FROM usuario u
                LEFT JOIN empresa e ON u.empresa_id = e.empresa_id
                LEFT JOIN producto p ON u.usuario_id = p.usuario_id
                LEFT JOIN factura_proveedor fp ON u.usuario_id = fp.usuario_id AND YEAR(fp.fecha_emision) = :anio
                LEFT JOIN compra comp ON u.usuario_id = comp.usuario_id AND YEAR(comp.fecha) = :anio AND comp.estado = 'PAGADO'
                WHERE u.rol_id IN (SELECT rol_id FROM rol WHERE rol = 'PROVEEDOR')
                  AND u.is_enabled = 1
                GROUP BY u.usuario_id, e.nit, u.numero_identificacion, e.razon_social, u.nombres, u.apellidos, 
                         e.ciudad, e.direccion, u.direccion
                ORDER BY totalFacturado DESC
            """, nativeQuery = true)
    List<CuentasPagarProjection> findCuentasPorPagar(@Param("anio") int anio);

    @Query(value = """
                SELECT 
                    COUNT(DISTINCT u.usuario_id) as totalProveedores,
                    COUNT(DISTINCT p.producto_id) as totalProductos,
                    COALESCE(SUM(fp.total), 0) as totalFacturadoAnual,
                    COUNT(DISTINCT comp.compra_id) as totalCompras
                FROM usuario u
                LEFT JOIN producto p ON u.usuario_id = p.usuario_id
                LEFT JOIN factura_proveedor fp ON u.usuario_id = fp.usuario_id AND YEAR(fp.fecha_emision) = :anio
                LEFT JOIN compra comp ON u.usuario_id = comp.usuario_id AND YEAR(comp.fecha) = :anio AND comp.estado = 'PAGADO'
                WHERE u.rol_id IN (SELECT rol_id FROM rol WHERE rol = 'PROVEEDOR')
                  AND u.is_enabled = 1
            """, nativeQuery = true)
    Object[] findTotalesGeneralesCuentasPagar(@Param("anio") int anio);
}
