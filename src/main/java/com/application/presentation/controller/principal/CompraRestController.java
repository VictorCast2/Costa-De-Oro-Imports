package com.application.presentation.controller.principal;

import com.application.configuration.custom.CustomUserPrincipal;
import com.application.persistence.entity.compra.enums.EEstado;
import com.application.presentation.dto.compra.request.CompraCreateRequest;
import com.application.presentation.dto.compra.response.CompraResponse;
import com.application.service.interfaces.CompraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/compras")
@CrossOrigin(origins = "*") // luego colocar la url cuando se despliegue
@RequiredArgsConstructor
public class CompraRestController {

    private final CompraService compraService;

    @PostMapping
    public ResponseEntity<?> createCompra(@AuthenticationPrincipal CustomUserPrincipal principal,
                                          @Valid @RequestBody CompraCreateRequest compraCreateRequest) {
        try {
            CompraResponse response = this.compraService.createCompra(principal, compraCreateRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }


}
