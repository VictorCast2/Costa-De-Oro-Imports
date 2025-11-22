package com.application.presentation.dto.peticion.response;

public record PeticionUsurarioResponse(
        String nombres,
        String apellidos,
        String correo,
        String telefono
) {
}
