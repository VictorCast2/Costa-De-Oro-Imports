package com.application.configuration.custom;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class CustomAuthFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {

        String mensajeError = "Credenciales Invalidas";

        // Manejo de errores provenientes del .formLogin()
        switch (exception) {
            case LockedException e -> mensajeError = "Tu cuenta está bloqueada.";
            case DisabledException e -> mensajeError = "Tu cuenta está deshabilitada.";
            case AccountExpiredException e -> mensajeError = "Tu sesión ha expirado";
            case CredentialsExpiredException e -> mensajeError = "Tu contraseña ha expirado";
            case BadCredentialsException e -> mensajeError = "Usuario o contraseña incorrectos";
            case AuthenticationServiceException e -> mensajeError = e.getMessage(); // Mensaje en caso no se valide el
                                                                                    // reCAPTCHA
            default -> mensajeError = "error de Autenticación";
        }

        // Manejo de errores provenientes de Oauth2Authentication
        if (exception instanceof OAuth2AuthenticationException oAuth2Ex) {
            String codigoError = oAuth2Ex.getError().getErrorCode();

            switch (codigoError) {
                case "access_denied" -> mensajeError = "Has cancelado el inicio del sesión con Google";
                case "invalid_token" -> mensajeError = "El token de acceso de Google no es válido";
                case "invalid_user_info_response" -> mensajeError = "No pudimos obtener tu perfil desde Google";
                default -> mensajeError = "ERROR: ha ocurrido un error en la autenticación con Google" + codigoError;
            }
        }

        response.sendRedirect("/auth/login?error=" + UriUtils.encode(mensajeError, StandardCharsets.UTF_8));

    }
}