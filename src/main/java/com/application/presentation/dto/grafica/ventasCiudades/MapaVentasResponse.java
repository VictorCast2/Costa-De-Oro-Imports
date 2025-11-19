package com.application.presentation.dto.grafica.ventasCiudades;

import java.util.List;

public record MapaVentasResponse(
        List<VentaCiudadMapaDTO> ventasPorCiudad
) {
}
