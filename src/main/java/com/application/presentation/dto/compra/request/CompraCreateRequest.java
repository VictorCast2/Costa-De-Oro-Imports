package com.application.presentation.dto.compra.request;

import com.application.persistence.entity.compra.enums.EMetodoPago;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public record CompraCreateRequest(
        @NotNull EMetodoPago metodoPago,
        @NotBlank String cuponDescuento,
        @Valid List<DetalleVentaRequest> detalleVentaRequests
) {
}
