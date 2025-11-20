package com.application.presentation.dto.grafica.ventasCiudades;

public record VentaCiudadMapaDTO(
        String ciudad,
        Long ventas,
        Double latitud,
        Double longitud,
        Long empresasCompradoras
) {
}
