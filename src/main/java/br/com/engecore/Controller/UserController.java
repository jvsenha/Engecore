package br.com.engecore.Controller;

import br.com.engecore.DTO.ApiResponse;
import br.com.engecore.DTO.UserDTO;
import br.com.engecore.Entity.UserEntity;
import br.com.engecore.Enum.Role;
import br.com.engecore.Enum.Status;
import br.com.engecore.Mapper.UserMapper;
import br.com.engecore.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UserController {

    @Autowired
    private UserService userService;

    // 🔹 Cadastrar usuário
    @PostMapping("/cadastrar")
    public ResponseEntity<ApiResponse<UserDTO>> cadastrar(@RequestBody UserDTO dto) {
        try {
            UserDTO user = userService.cadastrar(dto);
            return ResponseEntity.ok(new ApiResponse<>(true, "Usuário cadastrado com sucesso", user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // 🔹 Atualizar usuário (ADM ou FUNC.ADM)
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> atualizar(@PathVariable Long id, @RequestBody UserDTO dto) {
        try {
            UserDTO user = userService.atualizar(id, dto);
            return ResponseEntity.ok(new ApiResponse<>(true, "Usuário atualizado com sucesso", user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // 🔹 Alterar senha (ADM ou FUNC.ADM)
    @PutMapping("/{id}/alterar-senha")
    public ResponseEntity<ApiResponse<Void>> alterarSenha(@PathVariable Long id, @RequestParam String novaSenha) {
        try {
            userService.alterarSenhaAdmFunc(id, novaSenha);
            return ResponseEntity.ok(new ApiResponse<>(true, "Senha alterada com sucesso", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // 🔹 Deletar usuário (apenas ADM)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletar(@PathVariable Long id) {
        try {
            userService.deletarUsuario(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Usuário deletado com sucesso", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // 🔹 Buscar usuário por ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserEntity>> buscarPorId(@PathVariable Long id) {
        try {
            UserEntity user = userService.buscarUsuario(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Usuário encontrado", user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // 🔹 Listar todos os usuários
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDTO>>> listarTodos() {
        List<UserEntity> usuarios = userService.listarTodos();
        List<UserDTO> dtoList = usuarios.stream()
                .map(UserMapper::toUserDTO)
                .toList();

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Lista de usuários carregada com sucesso", dtoList)
        );
    }

    // 🔹 Listar usuários por Role
    @GetMapping("/role/{role}")
    public ResponseEntity<ApiResponse<List<UserEntity>>> listarPorRole(@PathVariable Role role) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Lista de usuários por role", userService.listarPorRole(role)));
    }

    // 🔹 Listar ativos
    @GetMapping("/ativos")
    public ResponseEntity<ApiResponse<List<UserEntity>>> listarAtivos() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Lista de usuários ativos", userService.listarAtivos()));
    }

    // 🔹 Listar inativos
    @GetMapping("/inativos")
    public ResponseEntity<ApiResponse<List<UserEntity>>> listarInativos() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Lista de usuários inativos", userService.listarInativos()));
    }

    // 🔹 Alternar status (ativo <-> inativo)
    @PutMapping("/{id}/alternar-status")
    public ResponseEntity<ApiResponse<Status>> alternarStatus(@PathVariable Long id) {
        try {
            Status status = userService.alternarStatus(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Status atualizado com sucesso", status));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}
