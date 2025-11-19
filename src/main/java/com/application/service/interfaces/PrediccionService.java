package com.application.service.interfaces;

import com.application.presentation.dto.venta.request.VentaRequest;

public interface PrediccionService {
    double predecir(VentaRequest request) throws Exception;
}