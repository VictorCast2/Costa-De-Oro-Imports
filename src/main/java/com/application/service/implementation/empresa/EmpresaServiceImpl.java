package com.application.service.implementation.empresa;

import com.application.configuration.custom.CustomUserPrincipal;
import com.application.persistence.entity.empresa.Empresa;
import com.application.persistence.entity.rol.Rol;
import com.application.persistence.entity.rol.enums.ERol;
import com.application.persistence.entity.usuario.Usuario;
import com.application.persistence.repository.EmpresaRepository;
import com.application.persistence.repository.RolRepository;
import com.application.persistence.repository.UsuarioRepository;
import com.application.presentation.dto.empresa.request.CreateEmpresaRequest;
import com.application.presentation.dto.empresa.request.SetEmpresaPhotoRequest;
import com.application.presentation.dto.empresa.request.UpdateEmpresaRequest;
import com.application.presentation.dto.general.response.GeneralResponse;
import com.application.service.implementation.ImagenServiceImpl;
import com.application.service.interfaces.CloudinaryService;
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

    private final CloudinaryService cloudinaryService;

    @Override
    @Transactional
    public GeneralResponse createEmpresa(CustomUserPrincipal principal, CreateEmpresaRequest empresaRequest) {

        String correo = principal.getCorreo();
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new EntityNotFoundException(
                        "El usuario autenticado con correo '" + correo + "' no existe."));

        Rol rolPersonaContacto = rolRepository.findByName(ERol.PERSONA_CONTACTO)
                .orElseThrow(() -> new EntityNotFoundException("error: el rol PERSONA_CONTACTO no existe"));

        String nit = empresaRequest.nit();
        boolean existeEmpresa = empresaRepository.existsByNit(nit);
        if (existeEmpresa) {
            return new GeneralResponse("error: La empresa con el nit '" + nit + "' ya tiene un usuario asignado.\n" +
                    "Si usted es el nuevo representante, escriba a admin@mail.com\n" +
                    " o use el formulario de contacto para solicitar la actualización.");
        }

        Empresa empresa = Empresa.builder()
                .imagen("perfil-empresa.png")
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

    @Override
    @Transactional
    public GeneralResponse updateEmpresa(CustomUserPrincipal principal, UpdateEmpresaRequest empresaRequest) {

        String correo = principal.getCorreo();
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new EntityNotFoundException(
                        "El usuario autenticado con correo '" + correo + "' no existe."));

        Empresa empresaActualizada = usuario.getEmpresa();
        if (empresaActualizada == null) {
            throw new EntityNotFoundException("El usuario no tiene una empresa asociada.");
        }

        empresaActualizada.setCiudad(empresaRequest.ciudad());
        empresaActualizada.setDireccion(empresaRequest.direccion());
        empresaActualizada.setTelefono(empresaRequest.telefono());
        empresaActualizada.setCorreo(empresaRequest.correo());
        empresaActualizada.setESector(empresaRequest.sector());

        usuario.setEmpresa(empresaActualizada);
        usuarioRepository.save(usuario);

        return new GeneralResponse("Empresa actualizada exitosamente");
    }

    @Override
    public GeneralResponse setEmpresaPhoto(CustomUserPrincipal principal, SetEmpresaPhotoRequest empresaPhotoRequest) {

        String correo = principal.getCorreo();
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new EntityNotFoundException(
                        "El usuario autenticado con correo '" + correo + "' no existe."));

        Empresa empresaPhoto = usuario.getEmpresa();
        if (empresaPhoto == null) {
            throw new EntityNotFoundException("El usuario no tiene una empresa asociada.");
        }

        String imagen = cloudinaryService.subirImagen(empresaPhotoRequest.imagenEmpresaNueva(), "perfil-empresa");
        if (imagen != null) {
            empresaPhoto.setImagen(imagen);
        } else {
            empresaPhoto.setImagen(empresaPhotoRequest.imagenEmpresaOriginal());
        }

        usuario.setEmpresa(empresaPhoto);
        usuarioRepository.save(usuario);

        return new GeneralResponse("Imagen asignada exitosamente");
    }

}