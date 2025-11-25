package com.application.repository;

import com.application.persistence.entity.usuario.Usuario;
import com.application.persistence.entity.usuario.enums.EIdentificacion;
import com.application.persistence.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.hibernate.hbm2ddl.auto=create-drop"
})
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario buildUsuario() {
        return Usuario.builder()
                .tipoIdentificacion(EIdentificacion.CC)
                .numeroIdentificacion("123456789")
                .nombres("Juan")
                .apellidos("Pérez")
                .telefono("3001234567")
                .correo("correo@prueba.com")
                .password("12345")
                .direccion("Calle falsa 123")
                .build();
    }

    @Test
    @DisplayName("Debe guardar un usuario y encontrarlo por correo")
    void testFindByCorreo() {
        // Arrange
        Usuario usuario = buildUsuario();
        usuarioRepository.save(usuario);

        // Act
        Optional<Usuario> resultado = usuarioRepository.findByCorreo("correo@prueba.com");

        // Assert
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getCorreo()).isEqualTo("correo@prueba.com");
    }

    @Test
    @DisplayName("Debe retornar true si existe un usuario con el correo dado")
    void testExistsByCorreoTrue() {
        // Arrange
        Usuario usuario = buildUsuario();
        usuarioRepository.save(usuario);

        // Act
        boolean exists = usuarioRepository.existsByCorreo("correo@prueba.com");

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Debe retornar false si NO existe un usuario con el correo dado")
    void testExistsByCorreoFalse() {
        // Act
        boolean exists = usuarioRepository.existsByCorreo("noexiste@correo.com");

        // Assert
        assertThat(exists).isFalse();
    }

}