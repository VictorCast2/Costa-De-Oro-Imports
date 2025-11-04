package com.application.presentation.dto.compra.response;

import com.application.persistence.entity.compra.enums.EEstado;
import com.application.persistence.entity.compra.enums.EMetodoPago;

import java.time.LocalDateTime;
import java.util.List;

public record CompraResponse(
        Long compraId,
        Long usuarioId,
        EMetodoPago metodoPago,
        double subtotal,
        String cuponDescuento,
        double iva,
        double total,
        LocalDateTime fecha,
        EEstado estado,
        List<DetalleVentaResponse> detalleVentaResponses
) {
}
