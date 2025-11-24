package com.application.presentation.dto.reporte.ventasPorCliente.response;

import java.math.BigDecimal;

public record VentasClienteResponse(
        String identificacion,
        String cliente,
        Long numeroComprobantes,
        BigDecimal valorBruto,
        BigDecimal impuestoCargo,
        BigDecimal total
) {
}
