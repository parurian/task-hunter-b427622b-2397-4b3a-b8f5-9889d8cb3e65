package dev.mher.taskhunter.controllers.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.mher.taskhunter.utils.ResponseUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: MheR
 * Date: 12/6/19.
 * Time: 2:37 PM.
 * Project: taskhunter.
 * Package: dev.mher.taskhunter.controllers.filters.
 */


public class AccessDeniedHandler implements AuthenticationEntryPoint {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        ResponseUtils responseBody = new ResponseUtils(true, "ACCESS_DENIED");
        response.getOutputStream().println(mapper.writeValueAsString(responseBody));
    }
}