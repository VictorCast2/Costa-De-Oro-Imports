package com.application.presentation.dto.grafica;

public record EstadisticasMensualesResponse(
        int año,
        int mes,
        Double ingresos,
        Double gastos
) {
}
