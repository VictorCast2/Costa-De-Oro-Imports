package com.application.presentation.dto.reporte.RentabilidadPorProducto.response;

import java.math.BigDecimal;

public record TotalGeneralRentabilidadResponse(
        BigDecimal ventasBrutasTotales,
        BigDecimal costosTotales
) {
}
