package com.application.presentation.dto.producto.request;

import java.util.List;

public record FiltroRequest(
        List<String> paises,
        List<String>marcas,
        List<String>categorias,
        List<String>subcategorias,
        Double precioMin,
        Double precioMax,
        String ordenarPor
) {
}
