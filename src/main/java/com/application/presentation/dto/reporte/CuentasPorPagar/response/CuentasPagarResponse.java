package com.application.presentation.dto.reporte.CuentasPorPagar.response;

import java.math.BigDecimal;

public record CuentasPagarResponse(
        String identificacion,
        String proveedor,
        String ciudad,
        String sucursal,
        Long productosSuministrados,
        Long facturasRegistradas,
        BigDecimal totalFacturado,
        Long comprasRealizadas
) {
}
