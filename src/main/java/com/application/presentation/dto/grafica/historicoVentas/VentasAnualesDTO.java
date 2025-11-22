package com.application.presentation.dto.grafica.historicoVentas;

public record VentasAnualesDTO(
        Integer año,
        Long cantidadVentas,
        Double ventasTotales,
        Double subtotalVentas,
        Double ivaTotal
) {
}
