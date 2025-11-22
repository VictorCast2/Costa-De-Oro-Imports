package com.application.presentation.dto.compra.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public record CompraCreateRequest(
        @NotBlank String metodoPago,
        @NotBlank String cuponDescuento,
        @Valid List<DetalleVentaRequest> detalleVentaRequests
) {
}