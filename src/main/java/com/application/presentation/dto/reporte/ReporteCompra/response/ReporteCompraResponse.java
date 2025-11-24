package com.application.presentation.dto.reporte.ReporteCompra.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReporteCompraResponse(
        String identificacion,
        String cliente,
        String ciudad,
        String sucursal,
        Long totalCompras,
        BigDecimal totalSubtotal,
        BigDecimal totalIva,
        BigDecimal totalComprado,
        BigDecimal promedioCompra,
        LocalDateTime ultimaCompra,
        String metodoPagoFrecuente
) {
}
