package com.application.service.implementation.comentario;

import com.application.configuration.custom.CustomUserPrincipal;
import com.application.persistence.entity.comentario.Comentario;
import com.application.persistence.entity.historia.Historia;
import com.application.persistence.entity.rol.enums.ERol;
import com.application.persistence.entity.usuario.Usuario;
import com.application.persistence.repository.ComentarioRepository;
import com.application.persistence.repository.HistoriaRepository;
import com.application.persistence.repository.UsuarioRepository;
import com.application.presentation.dto.comentario.request.ComentarioCreateRequest;
import com.application.presentation.dto.comentario.response.ComentarioBlogResponse;
import com.application.presentation.dto.comentario.response.ComentarioResponse;
import com.application.presentation.dto.comentario.response.ComentarioUsuarioResponse;
import com.application.presentation.dto.general.response.BaseResponse;
import com.application.service.interfaces.comentario.ComentarioService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComentarioServiceImpl implements ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final HistoriaRepository historiaRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * Obtener un comentario por id.
     * Este método es de uso interno para otros métodos del servicio
     *
     * @param id ID del comentario a buscar
     * @return la entidad comentario encontrada
     * @throws EntityNotFoundException si el comentario no exite
     */
    @Override
    public Comentario getComentarioById(Long id) {
        return comentarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("El comentario con id " + id + " no existe"));
    }

    /**
     * Obtiene todos los comentarios registrados (activos e inactivos).
     * Uso común para el panel administrativo, vista de reseñas.
     * Los datos a incluir son -> tituloBlog, nombreUsuario, mensaje, calificación y fecha
     *
     * @return Lista de DTOs con datos de cada comentario
     */
    @Override
    public List<ComentarioResponse> getComentarios() {
        List<Comentario> comentarios = comentarioRepository.findAll();
        return comentarios.stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Obtiene todos los comentarios activos pertenecientes a una historia por si id.
     * Uso común para la vista de descripción-historia.
     * Los datos a incluir son -> imagen (hay que agregarlo al response), nombresUsuario, correo,
     * título, mensaje, calificación
     *
     * @param historiaId ID de la historia a buscar
     * @return Lista de DTOs con los datos del comentario
     */
    @Override
    public List<ComentarioResponse> getComentariosActivosByHistoriaId(Long historiaId) {
        List<Comentario> comentarios = comentarioRepository.findByHistoria_HistoriaIdAndActivoTrue(historiaId);
        return comentarios.stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Crea un nuevo comentario a partir de un DTO de creación.
     *
     * @param historiaId ID de la historia a comentar
     * @param principal Usuario en sesión
     * @param comentarioRequest DTO con los datos del nuevo comentario
     * @return Respuesta con mensaje de confirmación y estado del proceso (success)
     * @throws EntityNotFoundException si la historia o el usuario no existen
     */
    @Override
    @Transactional
    public BaseResponse createComentario(Long historiaId, CustomUserPrincipal principal, ComentarioCreateRequest comentarioRequest) {

        Historia historia = historiaRepository.findById(historiaId)
                .orElseThrow(() -> new EntityNotFoundException("La historia con id: " + historiaId + " no existe."));

        Usuario usuario = usuarioRepository.findByCorreo(principal.getCorreo())
                .orElseThrow(() -> new EntityNotFoundException("El usuario autenticado con correo '" + principal.getCorreo() + "' no existe."));

        Comentario comentario = Comentario.builder()
                .titulo(comentarioRequest.titulo())
                .mensaje(comentarioRequest.mensaje())
                .calificacion(comentarioRequest.calificacion())
                .fecha(LocalDate.now())
                .activo(true)
                .build();

        comentario.addHistoria(historia);
        comentario.addUsuario(usuario);

        comentarioRepository.save(comentario);
        return new BaseResponse("Comentario creado exitosamente", true);
    }

    /**
     * Actualiza los datos de un comentario existente
     *
     * @param historiaId ID de la historia a validar si existe
     * @param comentarioId ID del comentario a actualizar
     * @param principal Usuario en sesión
     * @param comentarioRequest DTO con los datos del comentario actualizado
     * @return Respuesta con mensaje de confirmación y estado del proceso (success)
     * @throws EntityNotFoundException si la historia, comentario o el usuario no existen
     */
    @Override
    public BaseResponse updateComentario(Long historiaId, Long comentarioId, CustomUserPrincipal principal, ComentarioCreateRequest comentarioRequest) {

        Comentario comentario = this.getComentarioById(comentarioId);

        if (!comentario.getHistoria().getHistoriaId().equals(historiaId)) {
            throw new IllegalArgumentException("El comentario no pertenece a la historia indicada");
        }

        if (!comentario.getUsuario().getCorreo().equals(principal.getCorreo())) {
            throw new SecurityException("No puedes actualizar el comentario de otro usuario");
        }

        comentario.setTitulo(comentarioRequest.titulo());
        comentario.setMensaje(comentarioRequest.mensaje());
        comentario.setCalificacion(comentarioRequest.calificacion());

        comentarioRepository.save(comentario);

        return new BaseResponse("Comentario actualizado exitosamente", true);
    }

    /**
     * Cambia el estado del comentario.
     *
     * @param id ID del comentario a cambiar su estado
     * @return Respuesta con mensaje de confirmación y estado del proceso (success)
     * @throws EntityNotFoundException si el comentario no existe
     */
    @Override
    public BaseResponse changeEstadoComentario(Long id) {
        Comentario comentario = this.getComentarioById(id);

        boolean nuevoEstado = !comentario.isActivo();
        comentario.setActivo(nuevoEstado);
        comentarioRepository.save(comentario);

        String mensaje = nuevoEstado
                ? "Comentario habilitado exitosamente"
                : "Comentario deshabilitado exitosamente";

        return new BaseResponse(mensaje, true);
    }

    /**
     * Elimina un comentario y rompe su relación con historia y usuario para evitar
     * errores de integridad referencial en la base de datos.
     *
     * @param comentarioId ID del comentario a eliminar
     * @param principal Usuario en sesión
     * @return Respuesta con mensaje de confirmación y estado del proceso (success)
     * @throws EntityNotFoundException si el comentario no existe
     */
    @Override
    @Transactional
    public BaseResponse deleteComentario(Long comentarioId, CustomUserPrincipal principal) {
        Comentario comentario = this.getComentarioById(comentarioId);

        boolean esElDuenno = comentario.getUsuario().getCorreo().equals(principal.getCorreo());
        boolean esAdmin = principal.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!esElDuenno && !esAdmin) {
            return new BaseResponse("No puedes modificar un comentario que no es tuyo", false);
        }

        comentario.deleteHistoria(comentario.getHistoria());
        comentario.deleteUsuario(comentario.getUsuario());

        comentarioRepository.delete(comentario);
        return new BaseResponse("Comentario eliminado exitosamente", true);
    }

    /**
     * Convierte una entidad Comentario a su DTO de respuesta, incluyendo
     * algunos datos del Usuario y el título de la Historio.
     * Para uso interno del servicio en los métodos de búsqueda
     *
     * @param comentario Entidad comentario a convertir
     * @return DTO con la información completa del comentario
     */
    @Override
    public ComentarioResponse toResponse(Comentario comentario) {
        return new ComentarioResponse(
                comentario.getComentarioId(),
                comentario.getTitulo(),
                comentario.getMensaje(),
                comentario.getCalificacion(),
                comentario.getFecha(),
                comentario.isActivo(),
                new ComentarioUsuarioResponse(
                        comentario.getUsuario().getNombres(),
                        comentario.getUsuario().getApellidos(),
                        comentario.getUsuario().getCorreo()
                ),
                new ComentarioBlogResponse(
                        comentario.getHistoria().getTitulo()
                )
        );
    }
}
