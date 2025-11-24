package com.application.presentation.dto.factura.response;

public record FacturaProductoProveedorResponse(
        Long id,
        String nombre,
        String codigoProducto,
        String marca,
        Double precio,
        Integer stock
) {
}
