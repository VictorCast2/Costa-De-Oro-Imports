package com.application.service.interfaces;

import com.application.presentation.dto.DatosGraficaResponse;
import com.application.presentation.dto.venta.request.VentaRequest;
import java.util.Map;

public interface PrediccionService {
    double predecir(VentaRequest request) throws Exception;
    DatosGraficaResponse obtenerDatosParaGrafica();
    Map<String, Object> generarPrediccion2026();
}