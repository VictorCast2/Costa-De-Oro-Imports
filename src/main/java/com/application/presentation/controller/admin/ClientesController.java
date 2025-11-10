package com.application.presentation.controller.admin;

import com.application.configuration.custom.CustomUserPrincipal;
import com.application.persistence.entity.usuario.Usuario;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/clientes")
public class ClientesController {

    @GetMapping("/")
    public String DashboardClientes(@AuthenticationPrincipal CustomUserPrincipal principal,
                                     @RequestParam(value = "mensaje", required = false) String mensaje,
                                     Model model) {
        model.addAttribute("mensaje", mensaje);
        return "DashboardCategoria";
    }
    

}