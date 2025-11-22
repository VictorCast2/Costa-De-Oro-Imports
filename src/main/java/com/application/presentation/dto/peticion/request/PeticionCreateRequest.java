package com.application.presentation.dto.peticion.request;

import com.application.persistence.entity.pqrs.enums.EEstadoPeticion;
import com.application.persistence.entity.pqrs.enums.ETipoPeticion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public record PeticionCreateRequest(
        @NotNull ETipoPeticion tipoPeticion,
        @NotBlank String asunto,
        @NotBlank String mensaje,
        @NotNull EEstadoPeticion estadoPeticion
) {
}
