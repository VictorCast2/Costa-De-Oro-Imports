package com.application.presentation.controller.principal;

import com.application.configuration.custom.CustomUserPrincipal;
import com.application.persistence.entity.compra.Compra;
import com.application.presentation.dto.compra.request.CompraCreateRequest;
import com.application.presentation.dto.compra.response.CompraResponse;
import com.application.service.interfaces.CompraService;
import com.application.service.interfaces.MercadoPagoService;
import com.mercadopago.resources.preference.Preference;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/mercado-pago")
@CrossOrigin(origins = "*") // luego colocar la url cuando se despliegue
@RequiredArgsConstructor
public class MercadoPagoRestController {

    private final MercadoPagoService mercadoPagoService;
    private final CompraService compraService;

    @PostMapping("/iniciar-compra")
    public ResponseEntity<?> iniciarCompra(@AuthenticationPrincipal CustomUserPrincipal principal,
                                           @RequestBody CompraCreateRequest compraRequest,
                                           HttpServletRequest request) {
        try {
            // Creación de la compra
            CompraResponse compra = compraService.createCompra(principal, compraRequest);

            // Creación de la preferencia de pago
            Preference preference = mercadoPagoService.crearPreferenciaParaCompra(compra.compraId(), request);

            // Retornar respuesta al JS
            Map<String, Object> response = new HashMap<>();
            response.put("compraId", compra.compraId());
            response.put("preferenceId", preference.getId());
            response.put("initPoint", preference.getInitPoint());
            response.put("status", "success");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage(), "status", "error"));
        }
    }

    @PostMapping("/notificacion")
    public ResponseEntity<?> notificacion(@RequestParam Map<String, String> params) {
        try {
            String type = params.get("type");
            String dataId = params.get("data.id");

            if ("payment".equals(type)) {
                mercadoPagoService.procesarPago(dataId);
            }

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
