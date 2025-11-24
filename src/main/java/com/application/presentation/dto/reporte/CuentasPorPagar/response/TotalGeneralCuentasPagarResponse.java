package com.application.presentation.dto.reporte.CuentasPorPagar.response;

import java.math.BigDecimal;

public record TotalGeneralCuentasPagarResponse(
        Long totalProveedores,
        Long totalProductos,
        BigDecimal totalFacturadoAnual,
        Long totalCompras
) {
}
