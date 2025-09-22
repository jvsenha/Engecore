package br.com.engecore.Controller;

import br.com.engecore.DTO.ApiResponse;
import br.com.engecore.DTO.SenhaDTO;
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
@RequestMapping("/api/usuarios")
public class UserController {

    @Autowired
    private UserService userService;




    @PostMapping("/cadastrar")
    public ResponseEntity<ApiResponse<UserDTO>> cadastrar(@RequestBody UserDTO dto) {
        try {
            UserDTO user = userService.cadastrar(dto);
            return ResponseEntity.ok(new ApiResponse<>(true, "Usuário cadastrado com sucesso", user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PutMapping("/alterar/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> atualizarUser(@PathVariable Long id, @RequestBody UserDTO dto) {
        try {
            UserDTO user = userService.atualizar(id, dto);
            return ResponseEntity.ok(new ApiResponse<>(true, "Usuário atualizado com sucesso", user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PutMapping("/{id}/alterar-senha")
    public ResponseEntity<ApiResponse<String>> alterarSenha(@PathVariable Long id, @RequestBody SenhaDTO dto) {
        try {
            userService.alterarSenhaAdmFunc(id, dto.getNovaSenha());
            return ResponseEntity.ok(new ApiResponse<>(true, "Senha alterada com sucesso", dto.getNovaSenha()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @DeleteMapping("deletar/{id}")
    public ResponseEntity<ApiResponse<Void>> deletar(@PathVariable Long id) {
        try {
            userService.deletarUsuario(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Usuário deletado com sucesso", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserEntity>> buscarPorId(@PathVariable Long id) {
        try {
            UserEntity user = userService.buscarUsuario(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Usuário encontrado", user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<ApiResponse<List<UserDTO>>> listarTodos() {
        List<UserEntity> usuarios = userService.listarTodos();
        List<UserDTO> dtoList = usuarios.stream()
                .map(UserMapper::toUserDTO)
                .toList();

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Lista de usuários carregada com sucesso", dtoList)
        );
    }


    @GetMapping("/role/{role}")
    public ResponseEntity<ApiResponse<List<UserEntity>>> listarPorRole(@PathVariable Role role) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Lista de usuários por role", userService.listarPorRole(role)));
    }


    @GetMapping("listar/ativos")
    public ResponseEntity<ApiResponse<List<UserEntity>>> listarAtivos() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Lista de usuários ativos", userService.listarAtivos()));
    }


    @GetMapping("listar/inativos")
    public ResponseEntity<ApiResponse<List<UserEntity>>> listarInativos() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Lista de usuários inativos", userService.listarInativos()));
    }


    @PutMapping("/{id}/alterar-status")
    public ResponseEntity<ApiResponse<Status>> alternarStatus(@PathVariable Long id) {
        try {
            Status status = userService.alternarStatus(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Status atualizado com sucesso", status));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}
