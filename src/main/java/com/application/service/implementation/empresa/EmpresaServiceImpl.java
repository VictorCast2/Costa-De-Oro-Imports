package com.application.service.implementation.empresa;

import com.application.configuration.Custom.CustomUserPrincipal;
import com.application.persistence.entity.empresa.Empresa;
import com.application.persistence.entity.rol.Rol;
import com.application.persistence.entity.rol.enums.ERol;
import com.application.persistence.entity.usuario.Usuario;
import com.application.persistence.repository.EmpresaRepository;
import com.application.persistence.repository.RolRepository;
import com.application.persistence.repository.UsuarioRepository;
import com.application.presentation.dto.empresa.request.CreateEmpresaRequest;
import com.application.presentation.dto.general.response.GeneralResponse;
import com.application.service.interfaces.empresa.EmpresaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmpresaServiceImpl implements EmpresaService {

    private final UsuarioRepository usuarioRepository;
    private final EmpresaRepository empresaRepository;
    private final RolRepository rolRepository;

    @Override
    @Transactional
    public GeneralResponse createEmpresa(CustomUserPrincipal principal, CreateEmpresaRequest empresaRequest) {

        String correo = principal.getCorreo();
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new EntityNotFoundException("Error: el correo '" + correo + "' no existe."));

        Rol rolPersonaContacto = rolRepository.findByName(ERol.PERSONA_CONTACTO)
                .orElseThrow(() -> new EntityNotFoundException("Error: el rol PERSONA_CONTACTO no existe en la BD"));

        String nit = empresaRequest.nit();
        boolean existeEmpresa = empresaRepository.existsByNit(nit);

        if (existeEmpresa) {
            return new GeneralResponse("Error: La empresa con el nit '" + nit + "' ya tiene un usuario asignado.\n" +
                    "Si usted es el nuevo representante, escriba a admin@mail.com\n" +
                    " o use el formulario de contacto para solicitar la actualización.");
        }

        Empresa empresa = Empresa.builder()
                .nit(empresaRequest.nit())
                .razonSocial(empresaRequest.razonSocial())
                .ciudad(empresaRequest.ciudad())
                .direccion(empresaRequest.direccion())
                .telefono(empresaRequest.telefono())
                .correo(empresaRequest.correo())
                .eSector(empresaRequest.sector())
                .activo(true)
                .build();

        usuario.setEmpresa(empresa);
        usuario.setRol(rolPersonaContacto);

        usuarioRepository.save(usuario);

        return new GeneralResponse("Empresa registrada exitosamente");
    }
}