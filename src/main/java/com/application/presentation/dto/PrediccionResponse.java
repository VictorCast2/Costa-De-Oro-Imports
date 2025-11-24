package com.application.presentation.dto;

public record PrediccionResponse(
        Double prediccion,
        String mensaje,
        Boolean success
) {}