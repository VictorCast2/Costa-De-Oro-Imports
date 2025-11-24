package com.application.presentation.dto.reporte.ventasPorCliente.response;

import java.math.BigDecimal;

public record TotalGeneralResponse(
        BigDecimal valorBrutoTotal,
        BigDecimal impuestoCargoTotal,
        BigDecimal totalGeneral
) {
}
