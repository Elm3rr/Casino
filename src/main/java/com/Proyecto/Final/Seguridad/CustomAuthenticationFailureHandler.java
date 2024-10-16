package com.Proyecto.Final.Seguridad;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, 
                                        AuthenticationException exception) throws IOException, ServletException {
        String errorMessage;

        if (exception instanceof DisabledException) {
            errorMessage = "Tu cuenta está vetada o eliminada. Motivo: " + exception.getMessage();
        } else {
            errorMessage = "Credenciales incorrectas. Intenta de nuevo.";
        }

        // Redirigir al login con un mensaje de error
        request.getSession().setAttribute("errorMessage", errorMessage);
        response.sendRedirect(request.getContextPath() + "/login?error=true");
    }
}
