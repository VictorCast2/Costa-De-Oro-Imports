package com.application.presentation.dto.compra.request;

import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public record DetalleVentaRequest(
        @NotNull Long productoId,
        @NotNull int cantidad
) {
}
