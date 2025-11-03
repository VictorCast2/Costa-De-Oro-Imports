package com.application.service.interfaces;

import com.application.configuration.custom.CustomUserPrincipal;
import com.application.persistence.entity.compra.Compra;
import com.application.presentation.dto.compra.request.CompraCreateRequest;
import com.application.presentation.dto.compra.response.CompraResponse;

public interface CompraService {

    Compra getCompraById(Long compraId);

    CompraResponse createCompra(CustomUserPrincipal principal, CompraCreateRequest compraRequest);

    CompraResponse toResponse(Compra compra);
}
