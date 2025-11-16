package com.application.presentation.dto.compra.response;

public record DetalleProductoResponse(
        Long productoId,
        String nombre,
        String imagen,
        String marca,
        String tipo,
        double precioUnitario,
        int cantidad,
        double subtotal
) {
}
