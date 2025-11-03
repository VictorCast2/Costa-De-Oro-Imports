package com.application.service.interfaces;

import com.mercadopago.resources.preference.Preference;
import jakarta.servlet.http.HttpServletRequest;

public interface MercadoPagoService {

    Preference crearPreferenciaParaCompra(Long compraId, HttpServletRequest request);

    void procesarPago(String paymentId);

    String getBaseUrl(HttpServletRequest request);
}
