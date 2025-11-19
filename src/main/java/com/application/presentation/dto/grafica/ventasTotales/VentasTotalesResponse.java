package com.application.presentation.dto.grafica.ventasTotales;

import java.util.List;

public record VentasTotalesResponse(
        List<Double> series,
        List<String> labels,
        Double totalVentas
) {
}
