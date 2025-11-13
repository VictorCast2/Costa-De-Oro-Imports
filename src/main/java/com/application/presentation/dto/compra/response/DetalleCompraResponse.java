package com.application.presentation.dto.compra.response;

import java.time.LocalDateTime;
import java.util.List;

public record DetalleCompraResponse(
        Long compraId,
        String numeroOrden,
        String metodoPago,
        double subtotal,
        String cuponDescuento,
        double iva,
        double total,
        LocalDateTime fecha,
        String estado,

        // Datos del usuario
        Long usuarioId,
        String usuarioNombreCompleto,
        String usuarioCorreo,
        String usuarioTelefono,
        String usuarioDireccion,

        // Empresa direccion usuario
        String ciudadEmpresa,

        // Detalles del pedido (productos)
        List<DetalleProductoResponse> productos
) {
}
