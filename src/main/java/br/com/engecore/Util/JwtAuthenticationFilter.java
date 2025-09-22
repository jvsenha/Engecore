package br.com.engecore.Util;

import br.com.engecore.Entity.UserEntity;
import br.com.engecore.Repository.UserRepository;
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
    private UserRepository userRepository;

    @Autowired
    private TokenBlacklist tokenBlacklist;

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

        // Bloqueia se token estiver na blacklist
        if (token != null && tokenBlacklist.contains(token)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
            return;
        }

        // Se ainda não houver Authentication no contexto
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Valida token
            if (jwtUtil.validateToken(token, email)) {

                // Busca usuário no banco para pegar a role real
                UserEntity user = userRepository.findByEmail(email)
                        .orElse(null);

                if (user != null && user.getStatus() == br.com.engecore.Enum.Status.STATUS_ATIVO) {

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    user.getEmail(),
                                    null,
                                    Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()))
                            );

                    authToken.setDetails(new JwtAuthenticationDetails(
                            user.getId(),
                            user.getEmail(),
                            user.getRole().name()
                    ));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    public static class JwtAuthenticationDetails {
        private final Long userId;
        private final String email;
        private final String role;

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
