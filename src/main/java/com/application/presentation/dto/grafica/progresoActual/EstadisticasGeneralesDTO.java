package com.application.presentation.dto.grafica.progresoActual;

public record EstadisticasGeneralesDTO(
        // Datos actuales del semestre
        Double ingresosTotales,
        Double gastosTotales,
        Double beneficioTotal,
        Long stockDisponible,

        // Proyecciones basadas en semestre anterior
        Double proyeccionIngresosAnual,
        Double proyeccionGastosAnual,
        Double proyeccionBeneficioAnual,
        Long objetivoStock,

        // Porcentajes de progreso
        Integer progresoIngresos,
        Integer progresoGastos,
        Integer progresoBeneficio,
        Integer progresoStock,

        // Porcentajes de variación
        Double porcentajeVariacionIngresos,
        Double porcentajeVariacionGastos,
        Double porcentajeVariacionBeneficio,
        Double porcentajeVariacionStock
) {
}