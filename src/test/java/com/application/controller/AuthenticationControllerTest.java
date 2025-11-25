package com.application.controller;

import com.application.configuration.filter.RecaptchaFilter;
import com.application.configuration.security.TestSecurityConfig;
import com.application.presentation.controller.principal.AuthenticationController;
import com.application.service.interfaces.empresa.EmpresaService;
import com.application.service.interfaces.usuario.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = AuthenticationController.class, excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = RecaptchaFilter.class)
})
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class AuthenticationControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private UsuarioService usuarioService;

        @MockBean
        private EmpresaService empresaService;

        // ================== /auth/login ==================

        @Test
        void login_muestraVistaCorrecta() throws Exception {
                mockMvc.perform(get("/auth/login"))
                                .andExpect(status().isOk())
                                .andExpect(view().name("Login"));
        }

        @Test
        void login_conError_muestraMensajeError() throws Exception {
                mockMvc.perform(get("/auth/login")
                                .param("error", "true"))
                                .andExpect(status().isOk())
                                .andExpect(model().attributeExists("mensajeError"))
                                .andExpect(view().name("Login"));
        }

        @Test
        void login_conLogout_muestraMensajeExitoso() throws Exception {
                mockMvc.perform(get("/auth/login")
                                .param("logout", "true"))
                                .andExpect(status().isOk())
                                .andExpect(model().attributeExists("mensajeExitoso"))
                                .andExpect(view().name("Login"));
        }

        @Test
        void login_conSuccess_decodificaParams() throws Exception {
                String rolEncoded = Base64.getUrlEncoder()
                                .encodeToString("ADMIN".getBytes(StandardCharsets.UTF_8));
                String nextEncoded = Base64.getUrlEncoder()
                                .encodeToString("/index".getBytes(StandardCharsets.UTF_8));

                mockMvc.perform(get("/auth/login")
                                .param("success", "true")
                                .param("rol", rolEncoded)
                                .param("next", nextEncoded))
                                .andExpect(status().isOk())
                                .andExpect(model().attribute("loginSuccess", true))
                                .andExpect(model().attribute("rol", "ADMIN"))
                                .andExpect(model().attribute("next", "/index"))
                                .andExpect(view().name("Login"));
        }

        // ================== GET /auth/registrar-empresa ==================

        @Test
        @WithMockUser
        void registrarEmpresa_devuelveVistaCorrecta() throws Exception {
                mockMvc.perform(get("/auth/registrar-empresa"))
                                .andExpect(status().isOk())
                                .andExpect(model().attributeExists("sectores"))
                                .andExpect(view().name("RegistrarEmpresa"));
        }

        @Test
        @WithMockUser
        void registrarEmpresa_conMensaje_muestraMensaje() throws Exception {
                mockMvc.perform(get("/auth/registrar-empresa")
                                .param("mensaje", "Test message"))
                                .andExpect(status().isOk())
                                .andExpect(model().attribute("mensaje", "Test message"))
                                .andExpect(view().name("RegistrarEmpresa"));
        }

        // ================== GET /auth/registro ==================

        @Test
        void registro_devuelveVistaCorrecta() throws Exception {
                mockMvc.perform(get("/auth/registro"))
                                .andExpect(status().isOk())
                                .andExpect(model().attributeExists("tiposIdentificacion"))
                                .andExpect(view().name("Registro"));
        }

        @Test
        void registro_conMensajeYSuccess_muestraAtributos() throws Exception {
                mockMvc.perform(get("/auth/registro")
                                .param("mensaje", "Test message")
                                .param("success", "true"))
                                .andExpect(status().isOk())
                                .andExpect(model().attribute("mensaje", "Test message"))
                                .andExpect(model().attribute("success", true))
                                .andExpect(view().name("Registro"));
        }

        // ================== GET /auth/recordar-contrasenna ==================

        @Test
        void recordarContrasenna_devuelveVistaCorrecta() throws Exception {
                mockMvc.perform(get("/auth/recordar-contrasenna"))
                                .andExpect(status().isOk())
                                .andExpect(view().name("RecordarContrasena"));
        }
}