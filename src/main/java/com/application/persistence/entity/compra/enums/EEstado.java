package com.application.persistence.entity.compra.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EEstado {
    PENDIENTE("Pendiente"), // Pedido generado pero no pagado
    PAGADO("Pagado"), // Compra confirmada
    CANCELADO("Cancelado"), // Cancelación por usuario o tienda
    RECHAZADO("Rechazado"); // Rechazado por fondos insuficientes

    private final String descripcion;
}