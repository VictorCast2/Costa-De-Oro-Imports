package com.application.service.implementation.categoria;

import com.application.presentation.dto.general.response.BaseResponse;
import com.application.provider.CategoriaDataProvider;
import com.application.persistence.entity.categoria.Categoria;
import com.application.persistence.repository.CategoriaRepository;
import com.application.presentation.dto.categoria.request.CategoriaCreateRequest;
import com.application.presentation.dto.categoria.response.CategoriaResponse;
import com.application.presentation.dto.general.response.GeneralResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceImplTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaServiceImpl categoriaService;

    @Test
    void getCategoriaById() {
        // Given
        Long id = 1L;

        // When
        when(categoriaRepository.findById( anyLong() )).thenReturn(CategoriaDataProvider.categoriaMock());
        Categoria resultado = categoriaService.getCategoriaById(id);

        // Then
        assertNotNull(resultado);
        assertEquals("Categoria 1", resultado.getNombre());
        assertEquals("Primera categoria de prueba", resultado.getDescripcion());
        assertTrue(resultado.isActivo());

        verify(categoriaRepository).findById( anyLong() );
    }

    @Test
    void getCategoriaByIdError() {
        // Given
        Long id = 99L;
        when(categoriaRepository.findById( anyLong() )).thenReturn(Optional.empty());

        // When - Then
        NoSuchElementException ex = assertThrows(NoSuchElementException.class, () -> {
            categoriaService.getCategoriaById(id);
        });

        assertEquals("La categoria con id= " + id + " no existe o ha sido eliminada", ex.getMessage());
        verify(categoriaRepository).findById( anyLong() );
    }

    @Test
    void getCategorias() {
        // When
        when(categoriaRepository.findAll()).thenReturn(CategoriaDataProvider.categoriaListMock());
        when(categoriaRepository.countProductosByCategoriaId( anyLong() )).thenReturn(CategoriaDataProvider.cantidadPackPorCategoria());
        List<CategoriaResponse> resultado = categoriaService.getCategorias();

        // Then
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(5, resultado.size());
        assertEquals("Categoria 1", resultado.getFirst().nombre());
        assertEquals("Primera categoria de prueba", resultado.getFirst().descripcion());
        assertEquals(2, resultado.getFirst().totalProductos());

        verify(categoriaRepository).findAll();
        verify(categoriaRepository, times(5)).countProductosByCategoriaId( anyLong() );
    }

    @Test
    void getCategoriasActivas() {
        // When
        when(categoriaRepository.findByActivoTrue()).thenReturn(CategoriaDataProvider.categoriaActivaListMock());
        when(categoriaRepository.countProductosByCategoriaId( anyLong() )).thenReturn(CategoriaDataProvider.cantidadPackPorCategoria());
        List<CategoriaResponse> resultado = categoriaService.getCategoriasActivas();

        // Then
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(3, resultado.size());
        assertEquals("Categoria 1", resultado.getFirst().nombre());
        assertEquals("Primera categoria de prueba", resultado.getFirst().descripcion());
        assertEquals(2, resultado.getFirst().totalProductos());

        verify(categoriaRepository).findByActivoTrue();
        verify(categoriaRepository, times(3)).countProductosByCategoriaId( anyLong() );
    }

    @Test
    void createCategoria() {
        // Given
        CategoriaCreateRequest categoriaRequest = CategoriaDataProvider.newCategoriaMock();

        // When
        BaseResponse resultado = categoriaService.createCategoria(categoriaRequest);

        // Then
        ArgumentCaptor<Categoria> argumentCaptor = ArgumentCaptor.forClass(Categoria.class);
        verify(categoriaRepository).save( argumentCaptor.capture() );

        assertEquals("Categoria 1", argumentCaptor.getValue().getNombre());
        assertEquals("categoria creada exitosamente", resultado.mensaje());
    }

    @Test
    void updateCategoria() {
        // Given
        CategoriaCreateRequest categoriaRequest = new CategoriaCreateRequest(null,"categoria actualizada",  "categoria de pruebas",true, null);
        Long id = 1L;

        // When
        when(categoriaRepository.findById( anyLong() )).thenReturn(CategoriaDataProvider.categoriaMock());
        GeneralResponse resultado = categoriaService.updateCategoria(categoriaRequest, id);

        // Then
        ArgumentCaptor<Categoria> argumentCaptor = ArgumentCaptor.forClass(Categoria.class);
        verify(categoriaRepository).findById( anyLong() );
        verify(categoriaRepository).save( argumentCaptor.capture() );

        assertNotNull(argumentCaptor);
        assertEquals(1L, argumentCaptor.getValue().getCategoriaId());
        assertEquals("categoria actualizada", argumentCaptor.getValue().getNombre());
        assertEquals("categoria actualizada exitosamente", resultado.mensaje());
    }

    @Test
    void disableCategoria() {
        // Given
        Long id = 1L;

        // When
        when(categoriaRepository.findById( anyLong() )).thenReturn(CategoriaDataProvider.categoriaMock());
        BaseResponse resultado = categoriaService.changeEstadoCategoria(id);

        // Then
        ArgumentCaptor<Categoria> argumentCaptor = ArgumentCaptor.forClass(Categoria.class);
        verify(categoriaRepository).findById( anyLong() );
        verify(categoriaRepository).save(argumentCaptor.capture());

        assertFalse(argumentCaptor.getValue().isActivo());
        assertEquals("categoría deshabilitada exitosamente", resultado.mensaje());
    }

    @Test
    void deleteCategoria() {
        // Given
        Long id = 1L;

        // When
        when(categoriaRepository.findById( anyLong() )).thenReturn(CategoriaDataProvider.categoriaMock());
        BaseResponse resultado = categoriaService.deleteCategoria(id);

        // Then
        ArgumentCaptor<Categoria> argumentCaptor = ArgumentCaptor.forClass(Categoria.class);
        verify(categoriaRepository).findById( anyLong() );
        verify(categoriaRepository).delete(argumentCaptor.capture());

        assertNotNull(argumentCaptor.getValue());
        assertTrue(argumentCaptor.getValue().getProductos().isEmpty());
        assertEquals(1L, argumentCaptor.getValue().getCategoriaId());
        assertEquals("Categoria 1", argumentCaptor.getValue().getNombre());
        assertEquals("categoria eliminada exitosamente", resultado.mensaje());
    }

    @Test
    void deleteCategoriaError() {
        // Given
        Long id =  1L;

        // When - Then
        when(categoriaRepository.findById( anyLong() )).thenReturn(CategoriaDataProvider.categoriaConPackMock());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            categoriaService.deleteCategoria(id);
        });

        assertEquals("No es posible eliminar una categoria con productos", ex.getMessage());

        verify(categoriaRepository).findById( anyLong() );
    }
}