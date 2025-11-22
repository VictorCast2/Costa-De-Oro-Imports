package com.application.presentation.dto.grafica.columnasApiladas;

import java.util.List;

public record StockComprasResponse(
        List<String> categorias,
        List<Long> ventas,
        List<Long> stock
) {
}
