package com.application.controller.error;

import com.application.presentation.controller.error.GlobalErrorHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GlobalErrorHandlerTest {

    private GlobalErrorHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalErrorHandler();
    }

    @Test
    void handleCustom_debeRetornarVista300() {
        assertEquals("error/300", handler.handleCustom(300));
    }

    @Test
    void handleCustom_debeRetornarVista400() {
        assertEquals("error/400", handler.handleCustom(400));
    }

    @Test
    void handleCustom_debeRetornarVista401() {
        assertEquals("error/401", handler.handleCustom(401));
    }

    @Test
    void handleCustom_debeRetornarVista403() {
        assertEquals("error/403", handler.handleCustom(403));
    }

    @Test
    void handleCustom_debeRetornarVista404() {
        assertEquals("error/404", handler.handleCustom(404));
    }

    @Test
    void handleCustom_debeRetornarVista500() {
        assertEquals("error/500", handler.handleCustom(500));
    }

    @Test
    void handleCustom_codigoDesconocido_retornaDefault() {
        assertEquals("error/error", handler.handleCustom(999));
    }

    @Test
    void handleCustom_codigoNegativo_retornaDefault() {
        assertEquals("error/error", handler.handleCustom(-1));
    }

}
