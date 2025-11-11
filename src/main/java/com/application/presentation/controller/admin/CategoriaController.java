package com.application.presentation.controller.admin;

import com.application.configuration.custom.CustomUserPrincipal;
import com.application.persistence.entity.usuario.Usuario;
import com.application.presentation.dto.categoria.request.CategoriaCreateRequest;
import com.application.presentation.dto.categoria.response.CategoriaResponse;
import com.application.presentation.dto.general.response.BaseResponse;
import com.application.presentation.dto.general.response.GeneralResponse;
import com.application.presentation.dto.producto.response.ProductoResponse;
import com.application.service.implementation.categoria.CategoriaServiceImpl;
import com.application.service.implementation.usuario.UsuarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;
import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping("/admin/categoria")
public class CategoriaController {

    @Autowired
    private CategoriaServiceImpl categoriaService;

    @Autowired
    private UsuarioServiceImpl usuarioService;

    @GetMapping({"/", ""})
    public String DashboardCategoria(@AuthenticationPrincipal CustomUserPrincipal principal,
                                    @RequestParam(value = "mensaje", required = false) String mensaje,
                                    Model model) {

        Usuario usuario = usuarioService.getUsuarioByCorreo(principal.getUsername());

        model.addAttribute("usuario", usuario);
        model.addAttribute("mensaje", mensaje);

        return "DashboardCategoria";
    }

    @GetMapping("/add")
    public String AgregarCategoria(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(value = "mensaje", required = false) String mensaje,
            Model model) {
        Usuario usuario = usuarioService.getUsuarioByCorreo(userDetails.getUsername());
        List<CategoriaResponse> categoriaResponses = categoriaService.getCategorias();
        model.addAttribute("usuario", usuario);
        model.addAttribute("categoriaList", categoriaResponses);
        model.addAttribute("mensaje", mensaje);
        return "AgregarCategoria";
    }

    @GetMapping("/update")
    public String EditarCategoria(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(value = "mensaje", required = false) String mensaje,
            Model model) {
        Usuario usuario = usuarioService.getUsuarioByCorreo(userDetails.getUsername());
        List<CategoriaResponse> categoriaResponses = categoriaService.getCategorias();
        model.addAttribute("usuario", usuario);
        model.addAttribute("categoriaList", categoriaResponses);
        model.addAttribute("mensaje", mensaje);
        return "EditarCategoria";
    }

    @PostMapping("/add")
    public String addcategoria(@ModelAttribute @Valid CategoriaCreateRequest categoriaRequest) {
        GeneralResponse response = categoriaService.createCategoria(categoriaRequest);
        String mensaje = response.mensaje();

        return "redirect:/admin/categoria/?mensaje=" + UriUtils.encode(mensaje, StandardCharsets.UTF_8);
    }

    @PostMapping("update/{id}")
    public String updateCategoria(
            @ModelAttribute @Valid CategoriaCreateRequest categoriaRequest,
            @PathVariable Long id) {
        GeneralResponse response = categoriaService.updateCategoria(categoriaRequest, id);
        String mensaje = response.mensaje();

        return "redirect:/admin/categoria/?mensaje=" + UriUtils.encode(mensaje, StandardCharsets.UTF_8);
    }

    @PostMapping("disable/{id}")
    public String disableCategoria(@PathVariable Long id) {
        BaseResponse response = categoriaService.changeEstadoCategoria(id);
        String mensaje = response.mensaje();

        return "redirect:/admin/categoria/?mensaje=" + UriUtils.encode(mensaje, StandardCharsets.UTF_8);
    }

    @PostMapping("delete/{id}")
    public String deleteCategoria(@PathVariable Long id) {
        BaseResponse response = categoriaService.deleteCategoria(id);
        String mensaje = response.mensaje();
        return "redirect:/admin/categoria/?mensaje=" + UriUtils.encode(mensaje, StandardCharsets.UTF_8);
    }

}