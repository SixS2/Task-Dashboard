package com.dashboard.security;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class SecurityHeadersFilter implements Filter {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private String generateNonce() {
        byte[] nonceBytes = new byte[16];
        SECURE_RANDOM.nextBytes(nonceBytes);
        return Base64.getEncoder().encodeToString(nonceBytes);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Bloquear métodos HTTP inseguros (OPTIONS e TRACE)
        String method = httpRequest.getMethod().toUpperCase();
        if ("OPTIONS".equals(method) || "TRACE".equals(method)) {
            httpResponse.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }

        httpResponse.setHeader("X-Frame-Options", "DENY");
        httpResponse.setHeader("X-Content-Type-Options", "nosniff");
        httpResponse.setHeader("X-XSS-Protection", "1; mode=block");

        // Gerar nonce para CSP (scripts inline seguros)
        String nonce = generateNonce();
        httpRequest.setAttribute("cspNonce", nonce);

        String csp = "default-src 'self'; " +
                "script-src 'self' 'nonce-" + nonce + "' https://cdn.tailwindcss.com; " +
                "style-src 'self' 'unsafe-inline' https://fonts.googleapis.com; " +
                "font-src 'self' https://fonts.gstatic.com; " +
                "img-src 'self' data: blob:; " +
                "media-src 'self' blob:; " +
                "connect-src 'self'";
        httpResponse.setHeader("Content-Security-Policy", csp);

        httpResponse.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");

        // Evitar cache de dados sensíveis
        httpResponse.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        httpResponse.setHeader("Pragma", "no-cache");

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
