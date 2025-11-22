package com.application.presentation.controller.admin;

import com.application.configuration.custom.CustomUserPrincipal;
import com.application.persistence.entity.usuario.Usuario;
import com.application.presentation.dto.usuario.response.ProveedorEstadisticasResponse;
import com.application.service.implementation.usuario.UsuarioServiceImpl;
import com.application.service.interfaces.CloudinaryService;
import com.application.service.interfaces.usuario.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin/provedores")
@RequiredArgsConstructor
public class ProvedoresController {

    private final UsuarioService usuarioService;
    private final CloudinaryService cloudinaryService;

    @GetMapping({"", "/"})
    public String DashboardProvedores(@AuthenticationPrincipal CustomUserPrincipal principal,
                                      @RequestParam(value = "mensaje", required = false) String mensaje,
                                      @RequestParam(value = "success", required = false) Boolean success,
                                      Model model) {

        Usuario usuario = usuarioService.getUsuarioByCorreo(principal.getUsername());
        String urlImagenUsuario = cloudinaryService.getImagenUrl(usuario.getImagen());

        List<ProveedorEstadisticasResponse> proveedorList = usuarioService.getProveedorConEstadisticas();

        model.addAttribute("usuario", usuario);
        model.addAttribute("urlImagenUsuario", urlImagenUsuario);
        model.addAttribute("proveedorList", proveedorList);
        model.addAttribute("mensaje", mensaje);
        model.addAttribute("success", success);

        return "DashboardProvedores";
    }

    @GetMapping("/nueva-compra")
    public String nuevaCompra(@AuthenticationPrincipal CustomUserPrincipal principal,
                              Model model) {

        Usuario usuario = usuarioService.getUsuarioByCorreo(principal.getUsername());

        model.addAttribute("usuario", usuario);

        return "NuevaCompra";
    }

    @GetMapping("/add")
    public String agregarProvedores(@AuthenticationPrincipal CustomUserPrincipal principal,
                                    Model model) {

        Usuario usuario = usuarioService.getUsuarioByCorreo(principal.getUsername());

        model.addAttribute("usuario", usuario);

        return "AgregarProveedores";
    }

    @GetMapping("/update")
    public String actualizarProvedores(@AuthenticationPrincipal CustomUserPrincipal principal,
                                       Model model) {

        Usuario usuario = usuarioService.getUsuarioByCorreo(principal.getUsername());

        model.addAttribute("usuario", usuario);

        return "EditarProveedores";
    }

}