package com.dashboard.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.lang.NonNull;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        Object loggedIn = (session != null) ? session.getAttribute("admin_logged_in") : null;
        
        if (loggedIn != null && (Boolean) loggedIn) {
            // Check CSRF for POST requests
            if ("POST".equalsIgnoreCase(request.getMethod())) {
                String sessionToken = (session != null) ? (String) session.getAttribute("csrf_token") : null;
                String requestToken = request.getParameter("_csrf");
                
                if (sessionToken == null || !sessionToken.equals(requestToken)) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Token CSRF inválido ou ausente.");
                    return false;
                }
            }
            return true;
        }
        
        // Not logged in -> redirect to login
        response.sendRedirect("/login");
        return false;
    }
}
