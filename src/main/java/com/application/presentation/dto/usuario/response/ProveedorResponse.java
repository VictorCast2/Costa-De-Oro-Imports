package com.application.presentation.dto.usuario.response;

import com.application.persistence.entity.usuario.enums.EIdentificacion;

public record ProveedorResponse(
        // Datos del Usuario Proveedor
        Long proveedorId,
        EIdentificacion tipoIdentificacion,
        String numeroIdentificacion,
        String imagen,
        String nombres,
        String apellidos,
        String telefono,
        String correo,
        String password,
        String direccion,

        // Datos de la Empresa
        Long empresaId,
        String imagenEmpresa,
        String nit,
        String razonSocial,
        String ciudad,
        String direccionEmpresa,
        String telefonoEmpresa,
        String correoEmpresa
) {
}
