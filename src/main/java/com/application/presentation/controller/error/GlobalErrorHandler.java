package com.application.presentation.controller.error;

import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class GlobalErrorHandler {

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