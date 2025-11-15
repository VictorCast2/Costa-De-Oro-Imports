package com.application.presentation.controller.admin;

import com.application.configuration.custom.CustomUserPrincipal;
import com.application.persistence.entity.usuario.Usuario;
import com.application.presentation.dto.compra.response.CompraDashboardResponse;
import com.application.presentation.dto.compra.response.CompraResponse;
import com.application.presentation.dto.compra.response.DetalleCompraResponse;
import com.application.presentation.dto.general.response.BaseResponse;
import com.application.presentation.dto.producto.response.ProductoResponse;
import com.application.service.implementation.usuario.UsuarioServiceImpl;
import com.application.service.interfaces.CloudinaryService;
import com.application.service.interfaces.CompraService;
import com.application.service.interfaces.producto.ProductoService;
import com.application.service.interfaces.usuario.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/compra")
@RequiredArgsConstructor
public class CompraController {

    private final CompraService compraService;
    private final UsuarioService usuarioService;
    private final ProductoService productoService;
    private final CloudinaryService cloudinaryService;

    @GetMapping({"", "/"})
    public String DashboardCompra(@AuthenticationPrincipal CustomUserPrincipal principal,
                                  @RequestParam(value = "mensaje", required = false) String mensaje,
                                  Model model) {

        List<CompraDashboardResponse> compraList = compraService.getCompraByOrderAndFechaDesc();
        Usuario usuario = usuarioService.getUsuarioByCorreo(principal.getUsername());
        String urlImagenUsuario = cloudinaryService.getImagenUrl(usuario.getImagen());

        model.addAttribute("usuario", usuario);
        model.addAttribute("urlImagenUsuario", urlImagenUsuario);
        model.addAttribute("compras", compraList);
        model.addAttribute("mensaje", mensaje);

        return "DashboardCompra";
    }

    @GetMapping("/detalle-compra/{id}")
    public String DetalleCompra(@PathVariable Long id,
                                @AuthenticationPrincipal CustomUserPrincipal principal,
                                @RequestParam(name = "mensaje", required = false) String mensaje,
                                @RequestParam(name = "success", required = false) Boolean success,
                                Model model) {

        DetalleCompraResponse detalleCompra = compraService.getDetalleCompra(id);
        Usuario usuario = usuarioService.getUsuarioByCorreo(principal.getUsername());
        String urlImagenUsuario = cloudinaryService.getImagenUrl(usuario.getImagen());

        model.addAttribute("usuario", usuario);
        model.addAttribute("urlImagenUsuario", urlImagenUsuario);
        model.addAttribute("detalleCompra", detalleCompra);
        model.addAttribute("mensaje", mensaje);
        model.addAttribute("success", success);

        return "DetalleCompra";
    }

    @GetMapping("detalle-compra/cambiar-estado/{id}")
    public String changeEstadoCompra(@PathVariable Long id,
                                     @RequestParam String estado) {
        BaseResponse response = compraService.changeEstadoCompra(id, estado);
        String mensaje = UriUtils.encode(response.mensaje(), StandardCharsets.UTF_8);
        boolean success = response.success();
        return "redirect:/admin/compra/detalle-compra/{id}?mensaje=" + mensaje + "&success=" + success;
    }

}