package br.com.engecore.Service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service("securityService")
public class SecurityService {

    // ADMIN
    public boolean isAdmin(Authentication auth) {
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    // CLIENTE
    public boolean isCliente(Authentication auth) {
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CLIENTE"));
    }

    // FUNC (qualquer tipo de funcionário)
    public boolean isFuncionario(Authentication auth) {
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().startsWith("ROLE_FUNC"));
    }

    // FUNC específico: FUNC_ADM
    public boolean isFuncAdm(Authentication auth) {
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_FUNC_ADM"));
    }

    // FUNC específico: FUNC_FINANCEIRO
    public boolean isFuncFinanceiro(Authentication auth) {
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_FUNC_FINANCEIRO"));
    }

    // FUNC específico: FUNC_GERENTE
    public boolean isFuncGerente(Authentication auth) {
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_FUNC_GERENTE"));
    }

    // FUNC específico: FUNC_GESTOR
    public boolean isFuncGestor(Authentication auth) {
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_FUNC_GESTOR"));
    }

    // FUNC específico: FUNC_RH
    public boolean isFuncRh(Authentication auth) {
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_FUNC_RH"));
    }
}
