package com.application.presentation.controller.admin;

import com.application.configuration.custom.CustomUserPrincipal;
import com.application.persistence.entity.usuario.Usuario;
import com.application.presentation.dto.general.response.GeneralResponse;
import com.application.presentation.dto.producto.request.ProductoCreateRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

import javax.validation.Valid;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/admin/clientes")
public class ClientesController {

    @GetMapping({"/", ""})
    public String DashboardClientes(@AuthenticationPrincipal CustomUserPrincipal principal,
                                     @RequestParam(value = "mensaje", required = false) String mensaje,
                                     Model model) {
        model.addAttribute("mensaje", mensaje);
        return "DashboardClientes";
    }

    @GetMapping("/add-clientes")
    public String agregarClientes(@AuthenticationPrincipal CustomUserPrincipal principal,
                              Model model) {
        return "AgregarClientes";
    }

    @GetMapping("/update-clientes")
    public String editarClientes(@AuthenticationPrincipal CustomUserPrincipal principal,
                                 Model model) {
        return "EditarProducto";
    }

    @PostMapping("/add-clientes")
    public String addClientes() {
        String mensaje = "Clientes agregado exitosamente";
        String mensajeEncoded = UriUtils.encode(mensaje, StandardCharsets.UTF_8);
        return "redirect:/admin/clientes/?mensaje=" + mensajeEncoded;
    }

    @PostMapping("/update-clientes")
    public String updateClientes() {
        String mensaje = "Clientes editado exitosamente";
        String mensajeEncoded = UriUtils.encode(mensaje, StandardCharsets.UTF_8);
        return "redirect:/admin/clientes/?mensaje=" + mensajeEncoded;
    }

}