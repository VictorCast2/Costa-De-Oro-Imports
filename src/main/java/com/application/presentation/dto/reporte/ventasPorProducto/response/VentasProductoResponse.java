package com.application.presentation.dto.reporte.ventasPorProducto.response;

import java.math.BigDecimal;

public record VentasProductoResponse(
        String codigoProducto,
        String nombreProducto,
        String referenciaFabrica,
        Long cantidadVendida,
        BigDecimal valorBruto,
        BigDecimal impuestoCargo,
        BigDecimal total
) {
}
