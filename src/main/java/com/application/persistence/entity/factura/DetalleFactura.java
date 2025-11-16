package com.application.persistence.entity.factura;

import com.application.persistence.entity.producto.Producto;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "detalle_factura", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "producto_id", "factura_id" }, name = "uk_detalleFactura_factura")
})
public class DetalleFactura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detalle_factura_id")
    private Long detalleFacturaId;

    private int cantidad;
    private int precioCompra;
    private int subtotal;

    // Cardinalidad con la tabla producto
    @ManyToOne
    @JoinColumn(name = "producto_id", referencedColumnName = "producto_id", foreignKey = @ForeignKey(name = "fk_detalleFactura_producto"))
    private Producto producto;

    // Cardinalidad con la tabla FacturaProveedor
    @ManyToOne
    @JoinColumn(name = "factura_id", referencedColumnName = "factura_id", foreignKey = @ForeignKey(name = "fk_detalleFactura_factura"))
    private FacturaProveedor factura;
}
