package com.application.presentation.controller.admin;

import com.application.configuration.custom.CustomUserPrincipal;
import com.application.persistence.entity.usuario.Usuario;
import com.application.presentation.dto.venta.request.VentaRequest;
import com.application.service.implementation.PrediccionServiceImpl;
import com.application.service.implementation.compra.CompraServiceImpl;
import com.application.service.implementation.usuario.UsuarioServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Map;

@Controller
@RequestMapping("/admin/prediccion")
@RequiredArgsConstructor
public class PrediccionesController {

    private final UsuarioServiceImpl usuarioServiceImpl;
    private final PrediccionServiceImpl prediccionServiceImpl;
    private final CompraServiceImpl compraServiceImpl;

    @GetMapping({ "", "/" })
    public String DashboardPrediccion(@AuthenticationPrincipal CustomUserPrincipal principal,
                                      @RequestParam(value = "mensaje", required = false) String mensaje,
                                      Model model) {

        // Usuario Actual
        Usuario usuario = usuarioServiceImpl.getUsuarioByCorreo(principal.getUsername());

        // Compras Anuales
        Long comprasAnuales = compraServiceImpl.getTotalCompasAnuales();

        model.addAttribute("usuario", usuario);
        model.addAttribute("mensaje", mensaje);
        model.addAttribute("comprasAnuales", comprasAnuales);

        return "DashboardPredicciones";
    }

    @PostMapping("/generar")
    @ResponseBody
    public ResponseEntity<Double> generarPrediccion(@Valid @RequestBody VentaRequest request) {
        try {
            double resultado = prediccionServiceImpl.predecir(request);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

}