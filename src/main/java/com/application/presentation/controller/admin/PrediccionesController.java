package com.application.presentation.controller.admin;

import com.application.configuration.custom.CustomUserPrincipal;
import com.application.persistence.entity.usuario.Usuario;
import com.application.presentation.dto.venta.request.VentaRequest;
import com.application.service.implementation.PrediccionServiceImpl;
import com.application.service.implementation.usuario.UsuarioServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@Controller
@RequestMapping("/admin/prediccion")
@RequiredArgsConstructor
public class PrediccionesController {

    @Autowired
    private UsuarioServiceImpl usuarioService;

    @Autowired
    private final PrediccionServiceImpl prediccionServiceimpl;

    @GetMapping({ "", "/" })
    public String DashboardPrediccion(@AuthenticationPrincipal CustomUserPrincipal principal,
            @RequestParam(value = "mensaje", required = false) String mensaje,
            Model model) {

        Usuario usuario = usuarioService.getUsuarioByCorreo(principal.getUsername());

        model.addAttribute("usuario", usuario);
        model.addAttribute("mensaje", mensaje);

        return "DashboardPredicciones";
    }

    @PostMapping("/generar")
    @ResponseBody
    public ResponseEntity<Double> generarPrediccion(@Valid @RequestBody VentaRequest request) {
        try {
            double resultado = prediccionServiceimpl.predecir(request);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

}