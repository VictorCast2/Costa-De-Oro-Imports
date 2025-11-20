package com.application.service.interfaces.historia;

import com.application.persistence.entity.historia.Historia;
import com.application.presentation.dto.general.response.BaseResponse;
import com.application.presentation.dto.historia.request.HistoriaCreateRequest;
import com.application.presentation.dto.historia.response.HistoriaResponse;

import java.util.List;

public interface HistoriaService {
    // Consulta
    Historia getHistoriaById(Long id);

    HistoriaResponse getHistoriaResponseById(Long id);

    List<HistoriaResponse> getHistorias();

    List<HistoriaResponse> getHistoriasActivas();

    // CRUD
    BaseResponse createHistoria(HistoriaCreateRequest historiaRequest);

    BaseResponse updateHistoria(HistoriaCreateRequest historiaRequest, Long id);

    BaseResponse changeEstadoHistoria(Long id);

    BaseResponse deleteHistoria(Long id);

    // Utils
    HistoriaResponse toResponse(Historia historia);
}