package com.application.service.interfaces.usuario;

import com.application.configuration.custom.CustomUserPrincipal;
import com.application.persistence.entity.usuario.Usuario;
import com.application.presentation.dto.general.response.GeneralResponse;
import com.application.presentation.dto.general.response.BaseResponse;
import com.application.presentation.dto.usuario.request.*;
import com.application.presentation.dto.usuario.response.ClienteResponse;
import com.application.presentation.dto.usuario.response.ProveedorEstadisticasResponse;
import com.application.presentation.dto.usuario.response.ProveedorResponse;
import com.application.presentation.dto.usuario.response.UsuarioGastoResponse;

import java.util.List;

public interface UsuarioService {

    Usuario getUsuarioByCorreo(String correo);
    ClienteResponse getClienteById(Long clienteId);
    ProveedorResponse getProveedorById(Long proveedorId);
    List<UsuarioGastoResponse> getUsuarioGastoUltimoAnio();
    List<ProveedorEstadisticasResponse> getProveedorConEstadisticas();
    Long getTotalClientes();

    GeneralResponse completeUserProfile(CustomUserPrincipal principal, CompleteUsuarioProfileRequest completeProfileRequest);
    BaseResponse createUser(CreateUsuarioRequest usuarioRequest);
    BaseResponse createCliente(CreateClienteRequest clienteRequest);
    BaseResponse createProveedor(CreateProveedorRequest proveedorRequest);

    GeneralResponse updateUser(CustomUserPrincipal principal, UpdateUsuarioRequest usuarioRequest);
    BaseResponse updateCliente(Long clienteId, CreateClienteRequest clienteRequest);
    BaseResponse updateProveedor(Long proveedorId, CreateProveedorRequest proveedorRequest);

    GeneralResponse setUserPhoto(CustomUserPrincipal principal, SetUsuarioPhotoRequest usuarioPhotoRequest);
    BaseResponse updatePassword(CustomUserPrincipal principal, UpdatePasswordRequest passwordRequest);

    BaseResponse changeEstadoUsuario(Long usuarioId);
    BaseResponse deleteUsuario(Long usuarioId);
    
}