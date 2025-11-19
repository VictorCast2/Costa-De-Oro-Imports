package com.application.service.interfaces;

import com.application.configuration.custom.CustomUserPrincipal;
import com.application.persistence.entity.compra.Compra;
import com.application.persistence.entity.compra.enums.EEstado;
import com.application.presentation.dto.compra.request.CompraCreateRequest;
import com.application.presentation.dto.compra.response.CompraResponse;

public interface CompraService {

    Compra getCompraById(Long compraId);
    Double getIngresoAnual();
    Long getTotalCompasAnuales();

    CompraResponse createCompra(CustomUserPrincipal principal, CompraCreateRequest compraRequest);

    void updateEstadoCompra(Long compraId, EEstado estado);

    void updateStockProductoByCompraIdAndEstadoCompra(Long compraId, EEstado estado);

    void limpiarComprasConEstadoPendiente();

    CompraResponse toResponse(Compra compra);
}
