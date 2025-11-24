package com.application.presentation.dto.reporte.ReporteCompra.response;

import java.math.BigDecimal;

public record TotalGeneralComprasResponse(
        Long totalCompras,
        Long totalClientes,
        BigDecimal totalSubtotal,
        BigDecimal totalIva,
        BigDecimal totalComprado,
        BigDecimal promedioCompra
) {
}
