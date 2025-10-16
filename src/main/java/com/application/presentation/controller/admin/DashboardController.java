package com.application.presentation.controller.admin;

import com.application.configuration.Custom.CustomUserPrincipal;
import com.application.persistence.entity.usuario.Usuario;
import com.application.service.implementation.usuario.UsuarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/principal")
public class DashboardController {

    @Autowired
    private UsuarioServiceImpl usuarioService;

    @GetMapping("/")
    public String Dashboard(@AuthenticationPrincipal CustomUserPrincipal principal,
                           @RequestParam(value = "mensaje", required = false) String mensaje,
                           Model model) {

        Usuario usuario = usuarioService.getUsuarioByCorreo(principal.getUsername());

        model.addAttribute("usuario", usuario);
        model.addAttribute("mensaje", mensaje);

        return "Dashboard";
    }
}
