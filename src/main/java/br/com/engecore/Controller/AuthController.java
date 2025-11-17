package br.com.engecore.Controller;

import br.com.engecore.DTO.ApiResponse;
import br.com.engecore.DTO.LoginRequest;
import br.com.engecore.DTO.LoginResponse;
import br.com.engecore.DTO.UserDTO;
import br.com.engecore.Entity.UserEntity;
import br.com.engecore.Mapper.UserMapper;
import br.com.engecore.Service.UserService;
import br.com.engecore.Util.JwtAuthenticationFilter;
import br.com.engecore.Util.TokenBlacklist;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private TokenBlacklist tokenBlacklist;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest dto, HttpServletResponse response) {

        try {
            // Autentica e gera token
            LoginResponse loginResponse = userService.autenticar(dto);

            // Cria cookie com o token JWT
            ResponseCookie cookie = ResponseCookie.from("JWT_TOKEN", loginResponse.getToken())
                    .httpOnly(true)
                    .secure(false) // Mude para true se estiver usando HTTPS
                    .path("/")
                    .maxAge(24 * 60 * 60)
                    .sameSite("Strict") // "Strict" ou "Lax". "Strict" é mais seguro.
                    .build();

            response.addHeader("Set-Cookie", cookie.toString());

            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Login realizado com sucesso", loginResponse)
            );

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    /**
     * CORRIGIDO: Agora o logout busca o token do Header ou do Cookie,
     * e também limpa o cookie httpOnly no navegador.
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            HttpServletRequest request,  // Adicionado
            HttpServletResponse response // Adicionado
    ) {

        String token = null;

        // 1. Tenta pegar do Header (se o frontend enviar)
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        // 2. Se não achou no header, tenta pegar do Cookie (o browser envia)
        if (token == null && request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("JWT_TOKEN".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        // 3. Se não achou em lugar nenhum, retorna o erro 400
        if (token == null) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Token não fornecido", null));
        }

        // 4. Adiciona o token na blacklist
        tokenBlacklist.add(token);

        // 5. Cria um cookie expirado para instruir o browser a removê-lo
        ResponseCookie cookie = ResponseCookie.from("JWT_TOKEN", "")
                .httpOnly(true)
                .secure(false) // Deve ser igual ao do login
                .path("/")
                .maxAge(0) // Expira o cookie
                .sameSite("Strict")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok(new ApiResponse<>(true, "Logout realizado com sucesso", null));
    }

    /**
     * NOVO: Endpoint /me para validar a sessão e buscar dados do usuário.
     * Requerido pelo authService.checkAuthStatus()
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDTO>> getUsuarioLogado() {
        // O JwtAuthenticationFilter já validou o token (do cookie)
        // e colocou os dados de autenticação no SecurityContext.
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "Usuário não autenticado", null));
        }

        try {
            // Pegamos os detalhes (incluindo o ID) que o filtro injetou
            JwtAuthenticationFilter.JwtAuthenticationDetails details =
                    (JwtAuthenticationFilter.JwtAuthenticationDetails) auth.getDetails();
            Long userId = details.getUserId();

            // Buscamos o usuário no banco
            UserEntity user = userService.buscarUsuario(userId);

            // Retornamos um DTO seguro (sem senha)
            UserDTO userDTO = UserMapper.toUserDTO(user);

            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Usuário autenticado", userDTO)
            );
        } catch (Exception e) {
            // Se o getDetails() falhar ou o usuário não for encontrado no banco
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "Sessão inválida. Faça login novamente.", null));
        }
    }
}