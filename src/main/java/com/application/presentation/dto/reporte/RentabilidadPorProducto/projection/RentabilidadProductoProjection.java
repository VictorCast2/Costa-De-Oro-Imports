package com.application.presentation.dto.reporte.RentabilidadPorProducto.projection;

import java.math.BigDecimal;

public interface RentabilidadProductoProjection {
    String getTipo();
    String getCodigoProducto();
    String getNombreProducto();
    String getReferenciaFabrica();
    String getCategoria();
    Long getCantidadVendida();
    BigDecimal getVentasBrutasTotales();
    BigDecimal getCostosTotales();
    BigDecimal getUtilidadPesos();
    BigDecimal getPorcentajeUtilidad();
    BigDecimal getPorcentajeRentabilidad();
}
