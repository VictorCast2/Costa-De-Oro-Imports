package com.application.presentation.dto.grafica.ventasTotales;

public record VentasPorEstadoDTO(
        String estado,
        Long cantidad,
        Double montoTotal
) {
}
