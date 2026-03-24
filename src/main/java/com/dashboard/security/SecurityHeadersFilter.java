package com.dashboard.security;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SecurityHeadersFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Previne ataques de Clickjacking (garante que o site não possa ser embutido em um iframe)
        httpResponse.setHeader("X-Frame-Options", "DENY");

        // Previne Content Sniffing (MIME sniffing)
        httpResponse.setHeader("X-Content-Type-Options", "nosniff");

        // Habilita proteção contra Cross-Site Scripting (XSS) no navegador
        httpResponse.setHeader("X-XSS-Protection", "1; mode=block");

        // Content Security Policy (CSP):
        // Permite carregamento apenas de origens confiáveis (próprio site, Tailwind CDN, Google Fonts)
        String csp = "default-src 'self'; " +
                     "script-src 'self' 'unsafe-inline' https://cdn.tailwindcss.com; " +
                     "style-src 'self' 'unsafe-inline' https://fonts.googleapis.com; " +
                     "font-src 'self' https://fonts.gstatic.com; " +
                     "img-src 'self' data:; " +
                     "connect-src 'self'";
        httpResponse.setHeader("Content-Security-Policy", csp);

        // Força HTTPS (HSTS) - O Browser sempre lembrará de usar HTTPS no futuro (max-age = 1 ano)
        httpResponse.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");

        // Continua a cadeia de filtros e entrega a página
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Inicialização se necessária
    }

    @Override
    public void destroy() {
        // Limpeza se necessária
    }
}
