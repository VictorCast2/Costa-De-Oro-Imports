package com.application.presentation.dto.usuario.response;

public record UsuarioGastoResponse(
        Long usuarioId,
        String imagen,
        String nombreCompleto,
        String correo,
        String telefono,
        double totalGastado,
        boolean isEnabled
) {}
