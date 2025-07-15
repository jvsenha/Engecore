package br.com.engecore.Security;

import br.com.engecore.Annotation.RequireRole;
import br.com.engecore.Enum.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RequireRoleInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod method = (HandlerMethod) handler;
        RequireRole annotation = method.getMethodAnnotation(RequireRole.class);

        if (annotation == null) {
            return true; // Nenhuma restrição de role
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Usuário não autenticado");
            return false;
        }

        String roleFromToken = auth.getAuthorities().iterator().next().getAuthority(); // ex: "ROLE_FUNC_GESTOR"

        for (Role allowedRole : annotation.value()) {
            if (roleFromToken.equals(allowedRole.name())) {
                return true;
            }

            // Permitir qualquer FUNC se anotação permitir um FUNC
            if (allowedRole.isFuncionario() && Role.valueOf(roleFromToken).isFuncionario()) {
                return true;
            }
        }

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write("Acesso negado para a role: " + roleFromToken);
        return false;
    }
}
