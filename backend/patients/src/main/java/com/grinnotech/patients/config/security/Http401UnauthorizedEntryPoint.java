package com.grinnotech.patients.config.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Returns a 401 error code (Unauthorized) to the client.
 */
public class Http401UnauthorizedEntryPoint implements AuthenticationEntryPoint {

    /**
     * Always returns a 401 error code to the client.
     * @param request
     * @param response
     * @param arg
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException arg)
            throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access Denied");
    }
}
