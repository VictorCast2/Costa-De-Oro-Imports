package com.application.presentation.dto.historia.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

@Validated
public record HistoriaCreateRequest(
        @NotNull MultipartFile imagen,
        @NotBlank String titulo,
        @NotBlank String descripcion,
        @NotBlank String historiaCompleta,
        boolean activo
) {
}
