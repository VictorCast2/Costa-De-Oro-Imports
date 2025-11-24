package com.application.presentation.dto.reporte.CuentasPorPagar.projection;

import java.math.BigDecimal;

public interface CuentasPagarProjection {
    String getIdentificacion();
    String getProveedor();
    String getCiudad();
    String getSucursal();
    Long getProductosSuministrados();
    Long getFacturasRegistradas();
    BigDecimal getTotalFacturado();
    Long getComprasRealizadas();
}
