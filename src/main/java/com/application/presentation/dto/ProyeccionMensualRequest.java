package com.application.presentation.dto;

public record ProyeccionMensualRequest (
        Integer mes,
        String nombreMes,
        Double ventaPredicha
) {}
