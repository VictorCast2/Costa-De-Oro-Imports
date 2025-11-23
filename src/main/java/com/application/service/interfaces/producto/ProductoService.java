package com.application.service.interfaces.producto;

import com.application.persistence.entity.producto.Producto;
import com.application.presentation.dto.general.response.BaseResponse;
import com.application.presentation.dto.general.response.GeneralResponse;
import com.application.presentation.dto.producto.request.FiltroRequest;
import com.application.presentation.dto.producto.request.ProductoCreateRequest;
import com.application.presentation.dto.producto.response.ProductoResponse;
import java.util.List;
import java.util.Map;

public interface ProductoService {
    // Consulta
    Producto getProductoById(Long id);
    ProductoResponse getProductoResponseById(Long id);
    List<ProductoResponse> getProductos();
    List<ProductoResponse> getProductosActivos();
    List<ProductoResponse> getPacksActivos();
    List<ProductoResponse> getProductosMasVendidosActivos();
    List<ProductoResponse> getProductosMasVendidosByCategoriaIdActivos(Long categoriaId);
    List<ProductoResponse> getProductosMasVendidosByCategoriaIdsActivos(List<Long> categoriaIds);

    // Filtros
    List<String> getPaisesProducto();
    List<String> getMarcasProductos();
    List<ProductoResponse> getProductosPorPrecioAsc();
    List<ProductoResponse> getProductosPorPrecioDesc();
    List<ProductoResponse> getProductosPorNombreAsc();
    List<ProductoResponse> getProductosPorNombreDesc();
    Map<String, List<String>> getCategoriasActivasConSubcategorias();
    List<ProductoResponse> getProductosMasVendidos();
    List<ProductoResponse> filtrarProductos(FiltroRequest filtros);

    // CRUD
    BaseResponse createProducto(ProductoCreateRequest productoRequest);
    BaseResponse updateProducto(ProductoCreateRequest productoRequest, Long id);
    BaseResponse changeEstadoProducto(Long id);
    BaseResponse deleteProducto(Long id);

    // Utils
    ProductoResponse toResponse(Producto producto);
}