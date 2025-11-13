package com.application.presentation.controller.principal;

import com.application.configuration.custom.CustomUserPrincipal;
import com.application.persistence.entity.producto.Producto;
import com.application.persistence.entity.usuario.Usuario;
import com.application.presentation.dto.producto.response.ProductoResponse;
import com.application.service.implementation.producto.ProductoServiceImpl;
import com.application.service.implementation.usuario.UsuarioServiceImpl;
import com.application.service.interfaces.producto.ProductoService;
import com.application.service.interfaces.usuario.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class PrincipalController {

    private final UsuarioService usuarioService;
    private final ProductoService productoService;

    @GetMapping({""})
    public String index(Model model) {

        List<ProductoResponse> productosMasVendidos = productoService.getProductosMasVendidosActivos();

        List<ProductoResponse> productosConCategoriaVino = productoService.getProductosMasVendidosByCategoriaIdActivos(1L);
        List<ProductoResponse> productosConCategoriaWhisky = productoService.getProductosMasVendidosByCategoriaIdActivos(2L);
        List<ProductoResponse> productosConCategoriaCerveza = productoService.getProductosMasVendidosByCategoriaIdActivos(8L);

        List<ProductoResponse> productosConCategoriaVodkaGinebra = productoService.getProductosMasVendidosByCategoriaIdsActivos(List.of(4L, 6L));
        List<ProductoResponse> productosConCategoriaTequilaMezcal = productoService.getProductosMasVendidosByCategoriaIdsActivos(List.of(5L, 7L));
        List<ProductoResponse> productosConCategoriaRonAguardiente = productoService.getProductosMasVendidosByCategoriaIdsActivos(List.of(3L, 9L));

        model.addAttribute("productosMasVendidos", productosMasVendidos);
        model.addAttribute("productosConCategoriaVino", productosConCategoriaVino);
        model.addAttribute("productosConCategoriaWhisky", productosConCategoriaWhisky);
        model.addAttribute("productosConCategoriaCerveza", productosConCategoriaCerveza);
        model.addAttribute("productosConCategoriaVodkaGinebra", productosConCategoriaVodkaGinebra);
        model.addAttribute("productosConCategoriaTequilaMezcal", productosConCategoriaTequilaMezcal);
        model.addAttribute("productosConCategoriaRonAguardiente", productosConCategoriaRonAguardiente);
        return "Index";
    }

    @GetMapping("/cards")
    public String pruebacard() {
        return "pruebacard";
    }

    @GetMapping("/paks")
    public String pack(Model model) {

        List<ProductoResponse> packsActivos = productoService.getPacksActivos();

        model.addAttribute("packsActivos", packsActivos);

        return "Pack";
    }

    @GetMapping("/productos")
    public String producto(Model model) {

        List<ProductoResponse> productosActivos = productoService.getProductosActivos();

        model.addAttribute("productosActivos", productosActivos);
        return "Productos";
    }

    @GetMapping("/blog")
    public String blog() {
        return "Blog";
    }

    @GetMapping("/contacto")
    public String contacto() {
        return "Contactos";
    }

    @GetMapping("/carrito")
    public String carrito() {
        return "Carrito";
    }

    @GetMapping("/favorito")
    public String favorito() {
        return "Favorito";
    }

    @GetMapping("/ver")
    public String Ver(Model model) {
        return "Ver";
    }

    @GetMapping("/descripcion-producto/{productoId}")
    public String getDescripcionProducto(@AuthenticationPrincipal CustomUserPrincipal principal,
                                         @PathVariable Long productoId,
                                         Model model) {
        Usuario usuario = usuarioService.getUsuarioByCorreo(principal.getCorreo());
        ProductoResponse productoResponse = productoService.getProductoResponseById(productoId);

        List<ProductoResponse> productosActivos = productoService.getProductosActivos();
        Collections.shuffle(productosActivos);
        List<ProductoResponse> productosAleatorios = productosActivos.stream().limit(4).toList();

        model.addAttribute("usuario", usuario);
        model.addAttribute("producto", productoResponse);
        model.addAttribute("productoList", productosAleatorios);
        return "Aqui la vista de desccripcion del producto";
    }

}