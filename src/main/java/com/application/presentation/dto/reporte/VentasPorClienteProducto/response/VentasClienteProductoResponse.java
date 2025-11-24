package com.application.presentation.dto.reporte.VentasPorClienteProducto.response;

import java.math.BigDecimal;

public record VentasClienteProductoResponse(
        String identificacion,
        String cliente,
        String codigoProducto,
        String nombreProducto,
        String referenciaFabrica,
        Long cantidadVendida,
        BigDecimal valorBruto,
        BigDecimal impuestoCargo,
        BigDecimal total
) {
}
