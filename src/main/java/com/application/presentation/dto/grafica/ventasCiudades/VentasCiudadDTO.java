package com.application.presentation.dto.grafica.ventasCiudades;

public record VentasCiudadDTO(
        String ciudad,
        Long cantidadCompras,
        Long unidadesVendidas,
        Long empresasCompradoras
) {
}
