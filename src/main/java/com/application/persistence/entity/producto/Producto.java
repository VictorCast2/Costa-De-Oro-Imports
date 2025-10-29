package com.application.persistence.entity.producto;

import com.application.persistence.entity.categoria.Categoria;
import com.application.persistence.entity.categoria.SubCategoria;
import com.application.persistence.entity.compra.DetalleVenta;
import com.application.persistence.entity.producto.enums.ETipo;
import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "producto", uniqueConstraints = {
                @UniqueConstraint(columnNames = "codigo_producto", name = "uk_producto_codigo")
})
public class Producto {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "producto_id")
        private Long productoId;
        @Column(name = "codigo_producto")
        private String codigoProducto;
        private String imagen;
        private String nombre;
        private String marca;
        private String pais;
        @Column(name = "tipo")
        @Enumerated(EnumType.STRING)
        private ETipo eTipo;
        private double precio;
        @Column(name = "precio_regular")
        private double precioRegular;
        private int stock;
        @Column(length = 700)
        private String descripcion;
        private boolean activo;

        // Cardinalidad con la tabla categoria
        @ManyToOne
        @JoinColumn(name = "categoria_id",
                referencedColumnName = "categoria_id",
                foreignKey = @ForeignKey(name = "fk_producto_categoria")
        )
        private Categoria categoria;

        // Cardinalidad con la tabla sub-categoria
        @ManyToOne
        @JoinColumn(name = "subcategoria_id",
                referencedColumnName = "subcategoria_id",
                foreignKey = @ForeignKey(name = "fk_producto_subcategoria")
        )
        private SubCategoria subCategoria;

        // Cardinalidad con la tabla detálle ventas
        @Builder.Default
        @OneToMany(mappedBy = "producto", fetch = FetchType.LAZY)
        private Set<DetalleVenta> detalleVentas = new HashSet<>();

}