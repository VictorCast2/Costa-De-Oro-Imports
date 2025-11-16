package com.application.service.interfaces;

import com.application.configuration.custom.CustomUserPrincipal;
import com.application.persistence.entity.pqrs.enums.EEstadoPeticion;
import com.application.presentation.dto.general.response.BaseResponse;
import com.application.presentation.dto.peticion.request.PeticionCreateRequest;
import com.application.presentation.dto.peticion.response.PeticionResponse;

import java.util.List;

public interface PeticionService {

    // Consulta
    List<PeticionResponse> getPeticionesActivas();

    // CRUD
    BaseResponse createPeticion(CustomUserPrincipal principal, PeticionCreateRequest peticionRequest);
    BaseResponse updatePeticion(Long peticionId, CustomUserPrincipal principal, PeticionCreateRequest peticionRequest);
    BaseResponse changeEstadoPeticion(Long peticionId, String estadoPeticion);
    BaseResponse deletePeticion(Long peticionId);
}
