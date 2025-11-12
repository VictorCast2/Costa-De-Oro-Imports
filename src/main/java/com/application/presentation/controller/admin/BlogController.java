package com.application.presentation.controller.admin;

import com.application.configuration.custom.CustomUserPrincipal;
import com.application.persistence.entity.usuario.Usuario;
import com.application.service.implementation.usuario.UsuarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/admin/blog")
public class BlogController {

    @Autowired
    private UsuarioServiceImpl usuarioService;

    @GetMapping({"", "/"})
    public String DashboardBlog(@AuthenticationPrincipal CustomUserPrincipal principal,
                                  @RequestParam(value = "mensaje", required = false) String mensaje,
                                  Model model) {

        Usuario usuario = usuarioService.getUsuarioByCorreo(principal.getUsername());

        model.addAttribute("usuario", usuario);
        model.addAttribute("mensaje", mensaje);

        return "DashboardBlog";
    }

    @GetMapping("/add")
    public String agregarBlog(@AuthenticationPrincipal CustomUserPrincipal principal,
                                @RequestParam(value = "mensaje", required = false) String mensaje,
                                Model model) {

        Usuario usuario = usuarioService.getUsuarioByCorreo(principal.getUsername());

        model.addAttribute("usuario", usuario);
        model.addAttribute("mensaje", mensaje);

        return "AgregarBlog";
    }

    @GetMapping("/update")
    public String actualizarBlog(@AuthenticationPrincipal CustomUserPrincipal principal,
                              @RequestParam(value = "mensaje", required = false) String mensaje,
                              Model model) {

        Usuario usuario = usuarioService.getUsuarioByCorreo(principal.getUsername());

        model.addAttribute("usuario", usuario);
        model.addAttribute("mensaje", mensaje);

        return "EditarBlog";
    }

    @PostMapping("/add")
    public String addBlog() {
        String mensaje = "Blog editado exitosamente";
        String mensajeEncoded = UriUtils.encode(mensaje, StandardCharsets.UTF_8);
        return "redirect:/admin/blog/?mensaje=" + mensajeEncoded;
    }

    @PostMapping("/update")
    public String updateBlog() {
        String mensaje = "Blog editado exitosamente";
        String mensajeEncoded = UriUtils.encode(mensaje, StandardCharsets.UTF_8);
        return "redirect:/admin/blog/?mensaje=" + mensajeEncoded;
    }

}