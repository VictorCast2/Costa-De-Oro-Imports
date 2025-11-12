package com.application.presentation.controller.admin;

import com.application.presentation.dto.general.response.BaseResponse;
import com.application.provider.CategoriaDataProvider;
import com.application.persistence.entity.rol.Rol;
import com.application.persistence.entity.rol.enums.ERol;
import com.application.persistence.entity.usuario.Usuario;
import com.application.presentation.dto.categoria.request.CategoriaCreateRequest;
import com.application.presentation.dto.categoria.response.CategoriaResponse;
import com.application.presentation.dto.general.response.GeneralResponse;
import com.application.service.implementation.categoria.CategoriaServiceImpl;
import com.application.service.implementation.usuario.UsuarioServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoriaController.class)
class CategoriaControllerTest {

    @MockitoBean
    private CategoriaServiceImpl categoriaService;

    @MockitoBean
    private UsuarioServiceImpl usuarioService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void categoria() throws Exception {
        // Given
        Usuario usuarioMock = Usuario.builder()
                .numeroIdentificacion("12345")
                .nombres("Theresa Andrea")
                .correo("example@mail.com")
                .rol(Rol.builder().name(ERol.ADMIN).build())
                .build();

        List<CategoriaResponse> categoriasMock = CategoriaDataProvider.categoriaResponseListMock();

        String mensaje = "Mensaje de prueba";

        // When
        when(usuarioService.getUsuarioByCorreo("example@mail.com")).thenReturn(usuarioMock);
        when(categoriaService.getCategorias()).thenReturn(categoriasMock);

        // Then
        mockMvc.perform(get("/admin/categoria/")
                        .param("mensaje", mensaje)
                        .with(user("example@mail.com")
                                .roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("Categoria"))
                .andExpect(model().attribute("usuario", usuarioMock))
                .andExpect(model().attribute("categoriaList", categoriasMock))
                .andExpect(model().attribute("mensaje", mensaje));

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createCategoria() throws Exception {
        // Given
        BaseResponse responseMock = new BaseResponse("Mensaje de Prueba", true);
        String mensajeEncode = UriUtils.encode(responseMock.mensaje(), StandardCharsets.UTF_8);

        // When
        when(categoriaService.createCategoria( any(CategoriaCreateRequest.class) )).thenReturn(responseMock);

        // Then
        mockMvc.perform(post("/admin/categoria/add-categoria")
                        .with(csrf())
                        .param("nombre", "Categoria 1")
                        .param("descripcion", "Primera categoria de prueba"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/categoria/?mensaje=" + mensajeEncode));

        verify(categoriaService).createCategoria( any(CategoriaCreateRequest.class) );
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateCategoria() throws Exception {
        // Given
        BaseResponse resposeMock = new BaseResponse("Categoria Actualizada", true);
        String mensajeEconde = UriUtils.encode(resposeMock.mensaje(), StandardCharsets.UTF_8);
        Long id = 1L;

        // When
        when(categoriaService.updateCategoria( any(CategoriaCreateRequest.class), anyLong() )).thenReturn(resposeMock);

        // Then
        mockMvc.perform(post("/admin/categoria/update-categoria/{id}", id)
                        .with(csrf())
                        .param("nombre", "Categoria actualizada")
                        .param("descripcion", "Categoria de prueba actualizada"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/categoria/?mensaje=" + mensajeEconde));

        verify(categoriaService).updateCategoria( any(CategoriaCreateRequest.class), anyLong() );
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void disableCategoria() throws Exception {
        // Given
        BaseResponse responseMock = new BaseResponse("Categoria Deshabilitada", true);
        String mensajeEncode = UriUtils.encode(responseMock.mensaje(), StandardCharsets.UTF_8);
        Long id = 1L;

        // When
        when(categoriaService.changeEstadoCategoria( anyLong() )).thenReturn(responseMock);

        // Then
        mockMvc.perform(post("/admin/categoria/disable-categoria/{id}", id)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/categoria/?mensaje=" + mensajeEncode));

        verify(categoriaService).changeEstadoCategoria( anyLong() );
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteCategoria() throws Exception {
        // Given
        BaseResponse responseMock = new BaseResponse("Categoria Eliminada", true);
        String mensajeEncode = UriUtils.encode(responseMock.mensaje(), StandardCharsets.UTF_8);
        Long id = 1L;

        // When
        when(categoriaService.deleteCategoria( anyLong() )).thenReturn(responseMock);

        // Then
        mockMvc.perform(post("/admin/categoria/delete-categoria/{id}", id)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/categoria/?mensaje=" + mensajeEncode));

        verify(categoriaService).deleteCategoria( anyLong() );
    }
}