package com.application.provider;

import com.application.persistence.entity.categoria.Categoria;
import com.application.presentation.dto.categoria.request.CategoriaCreateRequest;
import com.application.presentation.dto.categoria.response.CategoriaResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoriaDataProvider {

    // Repository
    public static List<Categoria> categoriaList() {

        // ===== Categorías =====
        Categoria categoria1 = Categoria.builder()
                .nombre("Categoria 1")
                .descripcion("Primera categoria de prueba")
                .activo(true)
                .build();

        Categoria categoria2 = Categoria.builder()
                .nombre("Categoria 2")
                .descripcion("Segunda categoria de prueba")
                .activo(false)
                .build();

        Categoria categoria3 = Categoria.builder()
                .nombre("Categoria 3")
                .descripcion("Tercera categoria de prueba")
                .activo(true)
                .build();

        return new ArrayList<>(List.of(
                categoria1, categoria2, categoria3
        ));
    }

    // Service
    public static List<Categoria> categoriaListMock() {

        // ===== Categorías =====
        Categoria categoria1 = Categoria.builder()
                .categoriaId(1L)
                .nombre("Categoria 1")
                .descripcion("Primera categoria de prueba")
                .activo(true)
                .build();

        Categoria categoria2 = Categoria.builder()
                .categoriaId(2L)
                .nombre("Categoria 2")
                .descripcion("Segunda categoria de prueba")
                .activo(false)
                .build();

        Categoria categoria3 = Categoria.builder()
                .categoriaId(3L)
                .nombre("Categoria 3")
                .descripcion("Tercera categoria de prueba")
                .activo(true)
                .build();

        Categoria categoria4 = Categoria.builder()
                .categoriaId(4L)
                .nombre("Categoria 4")
                .descripcion("Cuarta categoria de prueba")
                .activo(false)
                .build();

        Categoria categoria5 = Categoria.builder()
                .categoriaId(5L)
                .nombre("Categoria 5")
                .descripcion("Quinta categoria de prueba")
                .activo(true)
                .build();

        return new ArrayList<>(List.of(
                categoria1, categoria2, categoria3, categoria4, categoria5
        ));
    }

    public static List<Categoria> categoriaActivaListMock() {

        // ===== Categorías =====
        Categoria categoria1 = Categoria.builder()
                .categoriaId(1L)
                .nombre("Categoria 1")
                .descripcion("Primera categoria de prueba")
                .activo(true)
                .build();

        Categoria categoria2 = Categoria.builder()
                .categoriaId(2L)
                .nombre("Categoria 2")
                .descripcion("Segunda categoria de prueba")
                .activo(true)
                .build();

        Categoria categoria3 = Categoria.builder()
                .categoriaId(3L)
                .nombre("Categoria 3")
                .descripcion("Tercera categoria de prueba")
                .activo(true)
                .build();

        return new ArrayList<>(List.of(
                categoria1, categoria2, categoria3
        ));
    }

    public static Optional<Categoria> categoriaMock() {
        return Optional.of(Categoria.builder()
                .categoriaId(1L)
                .nombre("Categoria 1")
                .descripcion("Primera categoria de prueba")
                .activo(true)
                .build());
    }

    public static Optional<Categoria> categoriaConPackMock() {
        Categoria categoria1 = Categoria.builder()
                .categoriaId(1L)
                .nombre("Categoria 1")
                .descripcion("Primera categoria de prueba")
                .activo(true)
                .build();

        return Optional.of(categoria1);
    }

    public static CategoriaCreateRequest newCategoriaMock() {
        return new CategoriaCreateRequest(null, "Categoria 1", "Primera  categoria de prueba", true, null);
    }

    public static long cantidadPackPorCategoria() {
        return 2L;
    }

    // Controller
    public static List<CategoriaResponse> categoriaResponseListMock() {
        return List.of(
                new CategoriaResponse(1L,"categoria.jpg","Categoria 1", "Primera Categoria de Pruebas", true, 5, null),
                new CategoriaResponse(2L, "categoria.jpg","Categoria 2", "Segunda Categoria de Pruebas", true, 3, null)
        );
    }
}