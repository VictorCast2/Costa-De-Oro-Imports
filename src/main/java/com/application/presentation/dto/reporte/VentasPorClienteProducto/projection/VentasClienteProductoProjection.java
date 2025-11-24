package com.application.presentation.dto.reporte.VentasPorClienteProducto.projection;

import java.math.BigDecimal;

public interface VentasClienteProductoProjection {
    String getIdentificacion();
    String getCliente();
    String getCodigoProducto();
    String getNombreProducto();
    String getReferenciaFabrica();
    Long getCantidadVendida();
    BigDecimal getValorBruto();
    BigDecimal getImpuestoCargo();
    BigDecimal getTotal();
}
