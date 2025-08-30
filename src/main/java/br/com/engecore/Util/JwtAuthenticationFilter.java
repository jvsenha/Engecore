package br.com.engecore.Util;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenBlacklist tokenBlacklist; // blacklist de tokens

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                email = jwtUtil.extractEmail(token);
            } catch (Exception e) {
                logger.error("Erro ao extrair email do token: " + e.getMessage());
            }
        }

        // Verifica se o token está na blacklist
        if (token != null && tokenBlacklist.contains(token)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
            return; // bloqueia a requisição
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtUtil.validateToken(token, email)) {
                String role = jwtUtil.extractRole(token);
                Long userId = jwtUtil.extractUserId(token);

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                Collections.singletonList(new SimpleGrantedAuthority(role))
                        );

                authToken.setDetails(new JwtAuthenticationDetails(userId, email, role));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }

    public static class JwtAuthenticationDetails {
        private Long userId;
        private String email;
        private String role;

        public JwtAuthenticationDetails(Long userId, String email, String role) {
            this.userId = userId;
            this.email = email;
            this.role = role;
        }

        public Long getUserId() { return userId; }
        public String getEmail() { return email; }
        public String getRole() { return role; }
    }
}
