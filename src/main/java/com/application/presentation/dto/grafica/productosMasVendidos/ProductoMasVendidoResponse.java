package com.application.presentation.dto.grafica.productosMasVendidos;

public record ProductoMasVendidoResponse(
        Long productoId,
        String nombre,
        String imagen,
        double precio,
        Long totalVentas
) {
}
