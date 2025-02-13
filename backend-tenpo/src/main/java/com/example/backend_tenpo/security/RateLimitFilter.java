package com.example.backend_tenpo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private static final ConcurrentHashMap<String, AtomicInteger> requestCountMap = new ConcurrentHashMap<>();
    private static final int MAX_REQUESTS = 3; // 🔹 Máximo 3 solicitudes por minuto
    private static final long TIME_WINDOW = 60 * 1000; // 🔹 1 minuto (60,000 ms)
    private static final ConcurrentHashMap<String, Long> lastRequestTimeMap = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        
        // ✅ Aplicar Rate Limiting solo a /api/transactions/**
        if (!requestURI.startsWith("/api/transactions")) {
            filterChain.doFilter(request, response);
            return;
        }

        String clientIp = request.getRemoteAddr();
        long currentTime = System.currentTimeMillis();

        // Comprobar si se debe restablecer el contador de solicitudes por IP
        lastRequestTimeMap.putIfAbsent(clientIp, currentTime);

        if (currentTime - lastRequestTimeMap.get(clientIp) > TIME_WINDOW) {
            requestCountMap.put(clientIp, new AtomicInteger(0));  // Restablecer el contador
            lastRequestTimeMap.put(clientIp, currentTime); // Actualizar el tiempo
        }

        // Asegurar que el contador no sea null, si lo es, inicializarlo en 0
        AtomicInteger requestCount = requestCountMap.computeIfAbsent(clientIp, k -> new AtomicInteger(0));

        // Verificar si el usuario ha superado el límite de solicitudes
        if (requestCount.incrementAndGet() > MAX_REQUESTS) {
            response.setStatus(429); // Código de estado 429: Demasiadas solicitudes
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Too many requests. Try again later.\"}");
            return; // 🔹 No continuar con la solicitud
        }

        // ✅ Permitir que la solicitud continúe
        filterChain.doFilter(request, response);
    }
}
