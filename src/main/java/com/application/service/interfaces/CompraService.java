package com.application.service.interfaces;

import com.application.configuration.custom.CustomUserPrincipal;
import com.application.persistence.entity.compra.Compra;
import com.application.persistence.entity.compra.enums.EEstado;
import com.application.presentation.dto.compra.request.CompraCreateRequest;
import com.application.presentation.dto.compra.response.CompraDashboardResponse;
import com.application.presentation.dto.compra.response.CompraResponse;
import com.application.presentation.dto.compra.response.DetalleCompraResponse;
import com.application.presentation.dto.general.response.BaseResponse;

import java.util.List;

public interface CompraService {

    Compra getCompraById(Long compraId);
    List<CompraDashboardResponse> getCompraByOrderAndFechaDesc();
    DetalleCompraResponse getDetalleCompra(Long compraId);

    CompraResponse createCompra(CustomUserPrincipal principal, CompraCreateRequest compraRequest);
    void updateEstadoCompra(Long compraId, EEstado estado);
    void updateStockProductoByCompraIdAndEstadoCompra(Long compraId, EEstado estado);
    void limpiarComprasConEstadoPendiente();
    BaseResponse changeEstadoCompra(Long compraId, String estado);

    CompraResponse toResponse(Compra compra);
}
