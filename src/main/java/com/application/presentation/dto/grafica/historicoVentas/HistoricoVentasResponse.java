package com.application.presentation.dto.grafica.historicoVentas;

import java.util.List;

public record HistoricoVentasResponse(
        List<Double> series,
        List<Integer> categorias,
        Double maxVenta,
        Double minVenta,
        Double promedioVentas
) {
}
