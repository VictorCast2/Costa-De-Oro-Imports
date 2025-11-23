package com.application.service.interfaces;

import com.application.persistence.entity.factura.FacturaProveedor;
import com.application.presentation.dto.factura.request.FacturaProveedorRequest;
import com.application.presentation.dto.factura.response.FacturaProductoProveedorResponse;
import com.application.presentation.dto.general.response.BaseResponse;

import java.util.List;

public interface FacturaProveedorService {

    List<FacturaProductoProveedorResponse> getProductosByProveedorId(Long proveedorId);
    BaseResponse crearFacturaCompra(FacturaProveedorRequest request);
    List<FacturaProveedor> getFacturasByProveedorId(Long proveedorId);
}
