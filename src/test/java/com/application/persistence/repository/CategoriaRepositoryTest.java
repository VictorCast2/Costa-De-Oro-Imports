package com.application.persistence.repository;

import com.application.provider.CategoriaDataProvider;
import com.application.persistence.entity.categoria.Categoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CategoriaRepositoryTest {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        List<Categoria> categorias = CategoriaDataProvider.categoriaList();

        // Persistir todos los packs de todas las categorías
        categorias.stream()
                .flatMap(categoria -> categoria.getProductos().stream())
                .forEach(entityManager::persist);

        // Persistir categorías sueltas (las que no vinieron por cascada)
        categorias.forEach(entityManager::persist);
        entityManager.flush();
    }

    @Test
    void findByActivoTrue() {
        // When
        List<Categoria> categoriasActivas = categoriaRepository.findByActivoTrue();

        // Then
        assertEquals(2, categoriasActivas.size());
        assertTrue(categoriasActivas.getFirst().isActivo());
        assertEquals("Categoria 1", categoriasActivas.getFirst().getNombre());
        assertFalse(categoriasActivas.isEmpty());
    }

    @Test
    void countPacksByCategoriaId() {
        // Given
        long categoriaId = 1L;

        // When
        long resultado = categoriaRepository.countProductosByCategoriaId(categoriaId);

        // Then
        assertEquals(2L, resultado);
    }
}