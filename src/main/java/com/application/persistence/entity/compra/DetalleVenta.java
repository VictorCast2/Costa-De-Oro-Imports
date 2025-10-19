package com.application.persistence.entity.compra;

import com.application.persistence.entity.producto.Producto;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "detalle_venta", uniqueConstraints = {
                @UniqueConstraint(columnNames = { "pack_id", "compra_id" }, name = "uk_detalleVenta_pack_compra")
})
public class DetalleVenta {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "detalle_venta_id")
        private Long detalleVentaId;

        private int cantidad;
        private int subtotal;

        // Cardinalidad con la tabla producto
        @ManyToOne
        @JoinColumn(name = "producto_id", referencedColumnName = "producto_id", foreignKey = @ForeignKey(name = "fk_detalleVenta_producto"))
        private Producto producto;

        // Cardinalidad con la table compra (relación unidireccional)
        @ManyToOne
        @JoinColumn(name = "compra_id", referencedColumnName = "compra_id", foreignKey = @ForeignKey(name = "fk_detalleVenta_compra"))
        private Compra compra;
}