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
                    .secure(true)
                    .path("/")
                    .maxAge(24 * 60 * 60)
                    .sameSite("Strict")
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

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Token não fornecido", null));
        }

        String token = authHeader.substring(7);
        tokenBlacklist.add(token);

        return ResponseEntity.ok(new ApiResponse<>(true, "Logout realizado com sucesso", null));
    }
}
