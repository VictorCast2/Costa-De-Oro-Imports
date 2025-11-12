package com.application.presentation.controller.admin;

import com.application.configuration.custom.CustomUserPrincipal;
import com.application.persistence.entity.usuario.Usuario;
import com.application.presentation.dto.general.response.GeneralResponse;
import com.application.presentation.dto.producto.request.ProductoCreateRequest;
import com.application.service.implementation.usuario.UsuarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private UsuarioServiceImpl usuarioService;

    @GetMapping({"/", ""})
    public String DashboardClientes(@AuthenticationPrincipal CustomUserPrincipal principal,
                                     @RequestParam(value = "mensaje", required = false) String mensaje,
                                     Model model) {
        Usuario usuario = usuarioService.getUsuarioByCorreo(principal.getUsername());

        model.addAttribute("usuario", usuario);
        model.addAttribute("mensaje", mensaje);
        return "DashboardClientes";
    }

    @GetMapping("/add")
    public String agregarClientes(@AuthenticationPrincipal CustomUserPrincipal principal,
                              Model model) {
        Usuario usuario = usuarioService.getUsuarioByCorreo(principal.getUsername());

        model.addAttribute("usuario", usuario);
        return "AgregarClientes";
    }

    @GetMapping("/update")
    public String editarClientes(@AuthenticationPrincipal CustomUserPrincipal principal,
                                 Model model) {
        Usuario usuario = usuarioService.getUsuarioByCorreo(principal.getUsername());

        model.addAttribute("usuario", usuario);
        return "EditarClientes";
    }

    @PostMapping("/add")
    public String addClientes() {
        String mensaje = "Clientes agregado exitosamente";
        String mensajeEncoded = UriUtils.encode(mensaje, StandardCharsets.UTF_8);
        return "redirect:/admin/clientes/?mensaje=" + mensajeEncoded;
    }

    @PostMapping("/update")
    public String updateClientes() {
        String mensaje = "Clientes editado exitosamente";
        String mensajeEncoded = UriUtils.encode(mensaje, StandardCharsets.UTF_8);
        return "redirect:/admin/clientes/?mensaje=" + mensajeEncoded;
    }

}