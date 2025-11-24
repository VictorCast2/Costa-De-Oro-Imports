package com.application.presentation.dto.reporte.ReporteCompra.projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ReporteCompraProjection {
    String getIdentificacion();
    String getCliente();
    String getCiudad();
    String getSucursal();
    Long getTotalCompras();
    BigDecimal getTotalSubtotal();
    BigDecimal getTotalIva();
    BigDecimal getTotalComprado();
    BigDecimal getPromedioCompra();
    LocalDateTime getUltimaCompra();
    String getMetodoPagoFrecuente();
}
