package com.application.service.interfaces;

import com.application.persistence.entity.compra.Compra;
import com.application.persistence.entity.compra.enums.EEstado;
import com.application.persistence.entity.usuario.Usuario;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface EmailService {

    void sendEmail(String to, String subject, String plantilla, Map<String, Object> variables);
    void sendWelcomeEmail(Usuario usuario);
    void sendEmailLoginSuccessful(Usuario usuario, HttpServletRequest request);
    void sendEmailEstadoPago(Usuario usuario, Compra compra, EEstado estado);

    // Utils
    String getDeviceInfo(HttpServletRequest request);
    String getTituloEstado(EEstado estado);
    String getMensajeEstado(EEstado estado);
    String getIconoEstado(EEstado estado);
}
