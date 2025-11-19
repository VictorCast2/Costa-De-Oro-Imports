package com.application.presentation.dto.venta.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record VentaRequest(

        @NotNull(message = "El año es obligatorio")
        Integer anio,

        @NotNull(message = "El mes es obligatorio")
        @Min(value = 1, message = "El mes mínimo es 1")
        @Max(value = 12, message = "El mes máximo es 12")
        Integer mes,

        @NotNull(message = "La cantidad de productos es obligatoria")
        @PositiveOrZero(message = "La cantidad de productos no puede ser negativa")
        Integer cantidadProductos,

        @NotNull(message = "Total de unidades es obligatorio")
        @PositiveOrZero(message = "Total de unidades no puede ser negativo")
        Integer totalUnidades,

        @NotNull(message = "El precio promedio es obligatorio")
        @PositiveOrZero(message = "El precio promedio no puede ser negativo")
        Double precioPromedio
) {
}