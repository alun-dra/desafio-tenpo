package com.example.backend_tenpo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    private static final List<String> PUBLIC_PATHS = Arrays.asList(
            "/swagger-ui/", "/api-docs", "/v3/api-docs", "/swagger-ui.html"
    );

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // 🔹 Permitir acceso sin autenticación a Swagger
        if (PUBLIC_PATHS.stream().anyMatch(path::startsWith)) {
            log.info("🔓 Permitiendo acceso sin autenticación a {}", path);
            chain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader("Authorization");

        log.info("🔹 Autenticación: Procesando solicitud a {}", path);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            log.warn("❌ Token no proporcionado o en formato incorrecto");
            chain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.replace("Bearer ", "");
        String username = null;
        try {
            username = jwtUtil.extractUsername(token);
        } catch (Exception e) {
            log.error("❌ Error al extraer el username del token: {}", e.getMessage());
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(token, userDetails)) {
                log.info("✅ Token válido para usuario: {}", username);
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                log.warn("❌ Token inválido o expirado para usuario: {}", username);
            }
        } else {
            log.warn("⚠️ No se encontró un usuario válido en el token.");
        }

        chain.doFilter(request, response);
    }
}
