package com.application.service;

import com.application.configuration.custom.CustomUserPrincipal;
import com.application.persistence.entity.rol.Rol;
import com.application.persistence.entity.rol.enums.ERol;
import com.application.persistence.entity.usuario.Usuario;
import com.application.persistence.entity.usuario.enums.EIdentificacion;
import com.application.persistence.repository.RolRepository;
import com.application.persistence.repository.UsuarioRepository;
import com.application.presentation.dto.general.response.BaseResponse;
import com.application.presentation.dto.general.response.GeneralResponse;
import com.application.presentation.dto.usuario.request.CreateUsuarioRequest;
import com.application.presentation.dto.usuario.request.SetUsuarioPhotoRequest;
import com.application.presentation.dto.usuario.request.UpdatePasswordRequest;
import com.application.presentation.dto.usuario.request.UpdateUsuarioRequest;
import com.application.service.implementation.usuario.UsuarioServiceImpl;
import com.application.service.interfaces.CloudinaryService;
import com.application.service.interfaces.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceImplTest {

    @Mock private UsuarioRepository usuarioRepository;
    @Mock private RolRepository rolRepository;
    @Mock private CloudinaryService cloudinaryService;
    @Mock private EmailService emailService;
    @Mock private PasswordEncoder encoder;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private Usuario usuario;
    private Rol rolNatural;
    private Rol rolJuridico;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        rolNatural = new Rol(1L, ERol.PERSONA_NATURAL);
        rolJuridico = new Rol(2L, ERol.PERSONA_JURIDICA);

        usuario = Usuario.builder()
                .usuarioId(1L)
                .correo("test@mail.com")
                .password("12345")
                .rol(rolNatural)
                .build();
    }

    // ============== loadUserByUsername ==============

    @Test
    void loadUserByUsername_usuarioExiste_devuelveUserDetails() {
        when(usuarioRepository.findByCorreo("test@mail.com"))
                .thenReturn(Optional.of(usuario));

        assertNotNull(usuarioService.loadUserByUsername("test@mail.com"));
    }

    @Test
    void loadUserByUsername_usuarioNoExiste_lanzaExcepcion() {
        when(usuarioRepository.findByCorreo("no@mail.com"))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
                usuarioService.loadUserByUsername("no@mail.com"));
    }

    // ============== getUsuarioByCorreo ==============

    @Test
    void getUsuarioByCorreo_existe_retornaUsuario() {
        when(usuarioRepository.findByCorreo("test@mail.com"))
                .thenReturn(Optional.of(usuario));

        Usuario result = usuarioService.getUsuarioByCorreo("test@mail.com");
        assertEquals("test@mail.com", result.getCorreo());
    }

    @Test
    void getUsuarioByCorreo_noExiste_lanzaExcepcion() {
        when(usuarioRepository.findByCorreo("x"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                usuarioService.getUsuarioByCorreo("x"));
    }

    // ============== createUser ==============

    @Test
    void createUser_correoExistente_retornaError() {
        when(usuarioRepository.existsByCorreo("test@mail.com"))
                .thenReturn(true);

        BaseResponse response = usuarioService.createUser(
                new CreateUsuarioRequest(EIdentificacion.CC,"1","a","b","1","test@mail.com","123")
        );

        assertFalse(response.success());
    }

    @Test
    void createUser_exitoso_retornaOk() {
        when(usuarioRepository.existsByCorreo("new@mail.com")).thenReturn(false);
        when(encoder.encode(any())).thenReturn("encrypted");
        when(rolRepository.findByName(any())).thenReturn(Optional.of(rolNatural));

        BaseResponse response = usuarioService.createUser(
                new CreateUsuarioRequest(EIdentificacion.CC,"1","a","b","1","new@mail.com","123")
        );

        assertTrue(response.success());
    }

    // ============== updateUser ==============

    @Test
    void updateUser_actualizaCorrectamente() {
        CustomUserPrincipal principal = new CustomUserPrincipal(usuario);

        when(usuarioRepository.findByCorreo(anyString())).thenReturn(Optional.of(usuario));

        GeneralResponse response = usuarioService.updateUser(
                principal,
                new UpdateUsuarioRequest(EIdentificacion.CC,"1","Nuevo","Apellido","123","correo@new.com")
        );

    }

    // ============== setUserPhoto ==============

    @Test
    void setUserPhoto_actualizaImagen() {
        CustomUserPrincipal principal = new CustomUserPrincipal(usuario);

        when(usuarioRepository.findByCorreo(anyString()))
                .thenReturn(Optional.of(usuario));

        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                new byte[]{1, 2}
        );

        when(cloudinaryService.subirImagen(any(MultipartFile.class), anyString()))
                .thenReturn("newImage.jpg");

        GeneralResponse response = usuarioService.setUserPhoto(
                principal,
                new SetUsuarioPhotoRequest(mockFile, "original.jpg")
        );

        assertEquals("Imagen asignada exitosamente", response.mensaje());
    }


    // ============== updatePassword ==============

    @Test
    void updatePassword_contrasenaActualIncorrecta() {
        CustomUserPrincipal principal = new CustomUserPrincipal(usuario);

        when(usuarioRepository.findByCorreo(anyString())).thenReturn(Optional.of(usuario));
        when(encoder.matches("wrong", usuario.getPassword())).thenReturn(false);

        BaseResponse response = usuarioService.updatePassword(
                principal,
                new UpdatePasswordRequest("wrong","newPass")
        );

        assertFalse(response.success());
    }

    @Test
    void updatePassword_exitosa() {
        CustomUserPrincipal principal = new CustomUserPrincipal(usuario);

        when(usuarioRepository.findByCorreo(anyString())).thenReturn(Optional.of(usuario));
        when(encoder.matches("12345", usuario.getPassword())).thenReturn(true);
        when(encoder.matches("newPass", usuario.getPassword())).thenReturn(false);
        when(encoder.encode("newPass")).thenReturn("encryptedNew");

        BaseResponse response = usuarioService.updatePassword(
                principal,
                new UpdatePasswordRequest("12345","newPass")
        );
        assertTrue(response.success());
    }

}