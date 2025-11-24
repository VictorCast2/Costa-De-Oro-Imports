package com.application.presentation.dto;

import java.util.List;
import java.util.Map;

public record DatosGraficaResponse (
        Map<String, double[]> ventasPorAño,
        Map<String, Object> estadisticas,
        List<ProyeccionMensualRequest> proyeccion2026
) {}
