package com.application.persistence.repository;

import com.application.persistence.entity.usuario.Usuario;
import com.application.presentation.dto.usuario.response.UsuarioGastoResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCorreo(String correo);

    boolean existsByCorreo(String correo);

    @Query("""
                SELECT new com.application.presentation.dto.usuario.response.UsuarioGastoResponse(
                    u.usuarioId,
                    u.imagen,
                    CONCAT(u.nombres, ' ', u.apellidos),
                    u.correo,
                    u.telefono,
                    COALESCE(SUM(c.total), 0),
                    u.isEnabled
                )
                FROM Usuario u
                LEFT JOIN u.compras c 
                    ON c.fecha >= :fechaInicio 
                    AND c.estado = com.application.persistence.entity.compra.enums.EEstado.PAGADO
                WHERE u.accountNonLocked = true
                AND u.rol.name IN ('INVITADO', 'PERSONA_CONTACTO', 'PERSONA_JURIDICA', 'PERSONA_NATURAL')
                GROUP BY u.usuarioId, u.imagen, u.nombres, u.apellidos, u.correo, u.telefono, u.isEnabled
                ORDER BY COALESCE(SUM(c.total), 0) DESC
            """)
    List<UsuarioGastoResponse> obtenerUsuariosConGastoUltimoAnio(@Param("fechaInicio") LocalDateTime fechaInicio);

    @Query(value = """
                SELECT COUNT(*) 
                FROM usuario u
                JOIN rol r ON u.rol_id = r.rol_id
                WHERE r.rol IN ('PERSONA_CONTACTO', 'PERSONA_JURIDICA', 'PERSONA_NATURAL')
            """, nativeQuery = true)
    Long totalClientes();

}