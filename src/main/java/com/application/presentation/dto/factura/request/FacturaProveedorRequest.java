package com.application.presentation.dto.factura.request;

import java.time.LocalDate;
import java.util.List;

public record FacturaProveedorRequest(
        String numeroFactura,
        LocalDate fechaEmision,
        List<DetalleFacturaRequest> detalles,
        Double totalCompra,
        Long proveedorId
) {
}
