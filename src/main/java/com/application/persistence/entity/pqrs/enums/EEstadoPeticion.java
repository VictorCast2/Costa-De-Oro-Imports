package com.application.persistence.entity.pqrs.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EEstadoPeticion {
    RESUELTO("Resuelto"),
    EN_PROCESO("En Proceso"),
    PENDIENTE("Pendiente");

    private final String descripcion;
}
