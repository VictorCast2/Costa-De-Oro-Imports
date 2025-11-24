package com.application.service.interfaces.categoria;

import com.application.persistence.entity.categoria.Categoria;
import com.application.presentation.dto.categoria.request.CategoriaCreateRequest;
import com.application.presentation.dto.categoria.response.CategoriaProductoResponse;
import com.application.presentation.dto.categoria.response.CategoriaResponse;
import com.application.presentation.dto.categoria.response.SubCategoriaProductoResponse;
import com.application.presentation.dto.general.response.BaseResponse;

import java.util.List;

public interface CategoriaService {

    // Consulta
    Categoria getCategoriaById(Long id);
    CategoriaResponse getCategoriaResponseById(Long id);
    List<CategoriaResponse> getCategorias();
    List<CategoriaResponse> getCategoriasActivas();
    List<CategoriaProductoResponse> getCategoriasProducto();
    List<SubCategoriaProductoResponse> getSubCategoriasActivasByCategoriaId(Long categoriaId);

    // CRUD
    BaseResponse createCategoria(CategoriaCreateRequest categoriaRequest);
    BaseResponse updateCategoria(CategoriaCreateRequest categoriaRequest, Long id);
    BaseResponse changeEstadoCategoria(Long id);
    BaseResponse deleteCategoria(Long id);

    // Utils
    CategoriaResponse toResponse(Categoria categoria);

}