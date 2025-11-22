package com.application.presentation.dto.grafica.comprasRecientes;

import java.time.LocalDateTime;

public record CompraResumenResponse(
        Long compraId,
        String numeroPedido,
        String nombreCliente,
        LocalDateTime fechaPedido,
        Double total,
        String estado
) {
    public String getClaseEstado() {
        return switch (estado.toUpperCase()) {
            case "PAGADO" -> "td__estados completado";
            case "PENDIENTE" -> "td__estados pendiente";
            case "CANCELADO" -> "td__estados cancelado";
            default -> "td__estados procesado";
        };
    }

    public String getTextoEstado() {
        return switch (estado.toUpperCase()) {
            case "PAGADO" -> "Pagado";
            case "PENDIENTE" -> "Pendiente";
            case "CANCELADO" -> "Cancelado";
            default -> "Rechazado";
        };
    }
}
