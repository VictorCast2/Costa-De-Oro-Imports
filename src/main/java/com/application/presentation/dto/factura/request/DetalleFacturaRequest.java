package com.application.presentation.dto.factura.request;

public record DetalleFacturaRequest(
        Long productoId,
        Integer cantidad,
        Integer precioCompra,
        Integer subtotal
) {
}
