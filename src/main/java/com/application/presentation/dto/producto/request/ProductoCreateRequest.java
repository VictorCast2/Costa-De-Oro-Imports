package com.application.presentation.dto.producto.request;

import com.application.persistence.entity.producto.enums.ETipo;
import jakarta.validation.constraints.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Validated
public record ProductoCreateRequest(
        @NotBlank String codigoProducto,
        @NotNull MultipartFile imagen,
        @NotBlank String nombre,
        @NotBlank String marca,
        @NotBlank String pais,
        @NotNull ETipo tipo,
        @NotNull @Positive double precioRegular,
        @NotNull @Positive double precio,
        @NotNull @PositiveOrZero int stock,
        @NotBlank String descripcion,
        boolean activo,
        // Datos de la categoria
        Long categoriaId,
        Long subCategoriaId,

        // Proveedor
        Long proveedorId
) {
}