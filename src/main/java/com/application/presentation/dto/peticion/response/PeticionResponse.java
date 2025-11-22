package com.application.presentation.dto.peticion.response;

import java.time.LocalDate;

public record PeticionResponse(
        Long id,
        String tipoPeticion,
        String asunto,
        String mensaje,
        LocalDate fecha,
        String estadoPeticion,
        boolean activo,
        PeticionUsurarioResponse usurarioResponse
) {
}
