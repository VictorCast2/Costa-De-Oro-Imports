package com.application.presentation.dto.reporte.RentabilidadPorProducto.response;

import java.math.BigDecimal;

public record RentabilidadProductoResponse(
        String tipo,
        String codigoProducto,
        String nombreProducto,
        String referenciaFabrica,
        String categoria,
        Long cantidadVendida,
        BigDecimal ventasBrutasTotales,
        BigDecimal costosTotales,
        BigDecimal utilidadPesos,
        BigDecimal porcentajeUtilidad,
        BigDecimal porcentajeRentabilidad
) {
}
