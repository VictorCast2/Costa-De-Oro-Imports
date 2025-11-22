package com.application.presentation.dto.usuario.response;

import com.application.persistence.entity.usuario.enums.EIdentificacion;

public record ClienteResponse(
        Long clienteId,
        EIdentificacion tipoIdentificacion,
        String numeroIdentificacion,
        String imagen,
        String nombres,
        String apellidos,
        String telefono,
        String correo,
        String password,
        String direccion
) {
}
