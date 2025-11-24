package com.application.presentation.dto.reporte.ventasPorProducto.projection;

import java.math.BigDecimal;

public interface VentasProductoProjection {
    String getCodigoProducto();
    String getNombreProducto();
    String getReferenciaFabrica();
    Long getCantidadVendida();
    BigDecimal getValorBruto();
    BigDecimal getImpuestoCargo();
    BigDecimal getTotal();
}
