package com.application.presentation.controller.error;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;

@ControllerAdvice
public class GlobalErrorHandler {

    // ⚠️ Error genérico 500
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "error/500";
    }

    // ❌ Página no encontrada (404)
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handle404() {
        return "error/404";
    }

    // 🚫 Acceso denegado (403)
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handle403() {
        return "error/403";
    }

    // 🚫 Método no permitido (405 → lo tratamos como 400)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handle405() {
        return "error/400";
    }

    // ⚠️ Todos las paginas de error personalizados
    public String handleCustom(int code) {
        return switch (code) {
            case 300 -> "error/300";
            case 400 -> "error/400";
            case 401 -> "error/401";
            case 403 -> "error/403";
            case 404 -> "error/404";
            case 500 -> "error/500";
            default -> "error/error";
        };
    }

}