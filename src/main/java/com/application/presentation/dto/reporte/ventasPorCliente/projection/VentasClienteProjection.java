package com.application.presentation.dto.reporte.ventasPorCliente.projection;

import java.math.BigDecimal;

public interface VentasClienteProjection {
    String getIdentificacion();
    String getCliente();
    Long getNumeroComprobantes();
    BigDecimal getValorBruto();
    BigDecimal getImpuestoCargo();
    BigDecimal getTotal();
}
