package com.application.persistence.entity.producto.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ETipo {
    UNIDAD("Unidad"), // Producto de una sola unidad
    CAJA("Caja"), // Caja de productos iguales, ej: 24 cervezas
    PACK("Pack"), // Pack de productos similares, ej: 6 cervezas variadas
    COMBO("Combo"); // Mezcla de diferentes tipos, ej: 2 vinos y 3 cervezas

    private final String descripcion;
}