package br.com.engecore.Controller;

import br.com.engecore.DTO.ApiResponse;
import br.com.engecore.DTO.LoginRequest;
import br.com.engecore.DTO.LoginResponse;
import br.com.engecore.Service.UserService;
import br.com.engecore.Util.TokenBlacklist;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController("/auth")
public class AuthController {

    @Autowired
    private TokenBlacklist tokenBlacklist;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @RequestBody LoginRequest dto,
            HttpServletResponse response) {

        try {
            // Autentica e gera token
            LoginResponse loginResponse = userService.autenticar(dto);

            // Cria cookie com o token JWT
            ResponseCookie cookie = ResponseCookie.from("JWT_TOKEN", loginResponse.getToken())
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(24 * 60 * 60)
                    .sameSite("Strict")
                    .build();

            // Adiciona o cookie na resposta
            response.addHeader("Set-Cookie", cookie.toString());

            // Retorna dados do login
            ApiResponse<LoginResponse> apiResponse = new ApiResponse<>(true, "Login realizado com sucesso", loginResponse);
            return ResponseEntity.ok(apiResponse);

        } catch (Exception e) {
            ApiResponse<LoginResponse> apiResponse = new ApiResponse<>(false, e.getMessage(), null);
            return ResponseEntity.badRequest().body(apiResponse);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        tokenBlacklist.add(token);
        return ResponseEntity.ok("Logout realizado com sucesso");
    }


}
