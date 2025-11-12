package com.application.service.interfaces.categoria;

import com.application.persistence.entity.categoria.Categoria;
import com.application.presentation.dto.categoria.request.CategoriaCreateRequest;
import com.application.presentation.dto.categoria.response.CategoriaResponse;
import com.application.presentation.dto.general.response.BaseResponse;
import com.application.presentation.dto.general.response.GeneralResponse;
import java.util.List;

public interface CategoriaService {

    // Consulta
    Categoria getCategoriaById(Long id);
    List<CategoriaResponse> getCategorias();
    List<CategoriaResponse> getCategoriasActivas();

    // CRUD
    BaseResponse createCategoria(CategoriaCreateRequest categoriaRequest);
    GeneralResponse updateCategoria(CategoriaCreateRequest categoriaRequest, Long id);
    BaseResponse changeEstadoCategoria(Long id);
    BaseResponse deleteCategoria(Long id);

    // Utils
    CategoriaResponse toResponse(Categoria categoria);

}