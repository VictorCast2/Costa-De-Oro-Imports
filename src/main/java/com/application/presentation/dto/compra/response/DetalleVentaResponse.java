package com.application.presentation.dto.compra.response;

public record DetalleVentaResponse(
        Long detalleVentaId,
        Long productoId,
        int precioUnitario,
        int cantidad,
        int subtotal
) {
}
