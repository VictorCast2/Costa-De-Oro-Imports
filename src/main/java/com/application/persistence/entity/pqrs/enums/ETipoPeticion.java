package com.application.persistence.entity.pqrs.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ETipoPeticion {
    PETICION("Peticion"),
    QUEJA("Queja"),
    RECLAMO("Reclamo"),
    SUGERENCIA("Sugerencia");

    private final String descripcion;
}
