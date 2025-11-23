package com.application.presentation.dto.usuario.response;

public record ProveedorEstadisticasResponse(
        Long usuarioId,
        String imagenEmpresa,
        String nombreEmpresa,
        String nombreCompleto,
        String correo,
        Double totalGastado,
        Double totalGanado,
        boolean isEnabled
) {
}
