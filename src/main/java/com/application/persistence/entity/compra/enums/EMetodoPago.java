package com.application.persistence.entity.compra.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EMetodoPago {
    MERCADO_PAGO("Mercado Pago"),
    TRANSFERENCIA("Transferencia"),
    TARJETA_CREDITO("Tarjeta Credito"),
    TARJETA_DEBITO("Tarjeta Debito"),
    PSE("PSE"),
    PAYPAL("PayPal"),
    NEQUI("Nequi"),
    DAVIPLATA("Daviplata");

    private final String descripcion;
}