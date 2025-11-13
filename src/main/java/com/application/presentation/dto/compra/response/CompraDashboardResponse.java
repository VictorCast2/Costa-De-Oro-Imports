package com.application.presentation.dto.compra.response;

import java.time.LocalDateTime;

public record CompraDashboardResponse(
        Long compraId,
        String numeroOrden,
        String usuarioNombreCompleto,
        String usuarioImagenUrl,
        String metodoPago,
        double total,
        LocalDateTime fecha,
        String estado
) {
}
