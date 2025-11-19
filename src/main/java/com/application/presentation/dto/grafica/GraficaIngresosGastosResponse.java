package com.application.presentation.dto.grafica;

import java.util.List;

public record GraficaIngresosGastosResponse(
        List<String> categorias, // Meses: Ene, Feb, Mar, etc.
        List<Double> ingresos,
        List<Double> gastos
) {
}
