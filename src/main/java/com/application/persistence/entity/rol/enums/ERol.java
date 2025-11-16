package com.application.persistence.entity.rol.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ERol {
    ADMIN("Admin"), // Administrador de la Aplicacion
    PROVEEDOR("Proveedor"), // Provee los productos
    INVITADO("Invitado"), // Usuario no logueado
    PERSONA_CONTACTO("Persona Contacto"), // Usuario afiliado a una empresa
    PERSONA_JURIDICA("Persona Jurídica"), // Usuario que cuanta con un NIT
    PERSONA_NATURAL("Persona Natural"); // Usuario registrado con un tipo de identificación diferente a NIT

    private final String descripcion;
}