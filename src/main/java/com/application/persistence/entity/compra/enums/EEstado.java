package com.application.persistence.entity.compra.enums;

public enum EEstado {
    PENDIENTE, // Pedido generado pero no pagado
    PAGADO, // Compra confirmada
    CANCELADO, // Cancelación por usuario o tienda
    RECHAZADO // Rechazado por fondos insuficientes
}