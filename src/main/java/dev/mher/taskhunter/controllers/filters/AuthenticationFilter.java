package dev.mher.taskhunter.controllers.filters;

import dev.mher.taskhunter.services.JwtService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

/**
 * User: MheR
 * Date: 12/4/19.
 * Time: 7:27 PM.
 * Project: taskhunter.
 * Package: dev.mher.taskhunter.controllers.filters.
 */

@Component
public class AuthenticationFilter extends OncePerRequestFilter {


    private final JwtService jwtService;

    public AuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        Optional.ofNullable(request.getHeader("X-Access-Token"))
                .flatMap(token -> Optional.ofNullable(jwtService.decode(token)))
                .ifPresent(output -> {
                    Integer userId = Integer.parseInt(output);
                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                userId,
                                null,
                                Collections.emptyList()
                        );
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                });
        filterChain.doFilter(request, response);
    }
}

