package br.com.engecore.Controller;

import br.com.engecore.DTO.ApiResponse;
import br.com.engecore.DTO.FuncionarioDTO;
import br.com.engecore.DTO.FuncionarioResponse;
import br.com.engecore.Entity.MovEstoqueEntity;
import br.com.engecore.Entity.MovFinanceiraEntity;
import br.com.engecore.Service.FuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/funcionario")
public class FuncionarioController {
    @Autowired
    private FuncionarioService funcionarioService;

    @PostMapping("/cadastrar")
    public ResponseEntity<ApiResponse<FuncionarioDTO>> cadastrarFuncionario(@RequestBody FuncionarioDTO dto) {
        FuncionarioDTO funcionario = funcionarioService.cadastrar(dto);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Funcionario cadastrado com sucesso", funcionario)
        );
    }

    @PutMapping("/alterar/{id}")
    public ResponseEntity<ApiResponse<FuncionarioDTO>> atualizarFuncionario(
            @PathVariable Long id,
            @RequestBody FuncionarioDTO dto
    ) {
        FuncionarioDTO funcionarioAtualizado = funcionarioService.atualizarPorAdmFuncionario(id, dto);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Funcionario atualizado com sucesso", funcionarioAtualizado)
        );
    }

    @PutMapping("/alterar/perfil")
    public ResponseEntity<ApiResponse<FuncionarioDTO>> atualizarPerfil(@RequestBody FuncionarioDTO dto) {
        FuncionarioDTO funcionarioAtualizado = funcionarioService.atualizarPerfilFuncionario(dto);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Perfil atualizado com sucesso", funcionarioAtualizado)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FuncionarioDTO>> buscarFuncionario(@PathVariable Long id) {
        FuncionarioDTO funcionario = funcionarioService.detalhesFuncionario(id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Funcionario encontrado", funcionario)
        );
    }
    @GetMapping("/listar")
    public ResponseEntity<ApiResponse<List<FuncionarioResponse>>> listar() {
        List<FuncionarioResponse> funcionarios = funcionarioService.listar();
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Funcionario encontrado", funcionarios)
        );
    }

    @GetMapping("/listar/movestoque/{id}")
    public ResponseEntity<ApiResponse<List<MovEstoqueEntity>>> listarMovEst(@PathVariable Long id) {
        List<MovEstoqueEntity> mov = funcionarioService.historicoMovimentacoesEstoque(id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Funcionario encontrado", mov)
        );
    }

    @GetMapping("/listar/movfinanceiro/{id}")
    public ResponseEntity<ApiResponse<List<MovFinanceiraEntity>>> listarMovFin(@PathVariable Long id) {
        List<MovFinanceiraEntity> mov = funcionarioService.historicoMovimentacoesFinanceira(id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Funcionario encontrado", mov)
        );
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<ApiResponse<Void>> deletarFuncionario(@PathVariable Long id) {
        funcionarioService.deletarFuncionario(id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Funcionario deletado com sucesso", null)
        );
    }



}
