package br.com.engecore.Controller;

import br.com.engecore.DTO.MovEstoqueDTO;
import br.com.engecore.DTO.MovEstoqueResponse;
import br.com.engecore.Service.MovEstoqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movEstoque")
public class MovEstoqueController {

    @Autowired
    private MovEstoqueService movEstoqueService;

    // Criar movimentação (entrada, saída, transferência, ajuste)
    @PostMapping("/cadastrar")
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public ResponseEntity<MovEstoqueDTO> criarMovimentacao(@RequestBody MovEstoqueDTO dto) {
        MovEstoqueDTO novaMov = movEstoqueService.cadastrar(dto);
        return ResponseEntity.ok(novaMov);
    }

    // Listar todas as movimentações
    @GetMapping("/listar")
    public ResponseEntity<List<MovEstoqueResponse>> listarMovimentacoes() {
        return ResponseEntity.ok(movEstoqueService.listarMovEstoque());
    }

    // Detalhar movimentação específica
    @GetMapping("/{id}")
    public ResponseEntity<MovEstoqueDTO> detalhes(@PathVariable Long id) {
        return ResponseEntity.ok(movEstoqueService.detalhesMovEstoque(id));
    }

    // Atualizar movimentação (reverte antiga e aplica nova)
    @PutMapping("/alterar/{id}")
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public ResponseEntity<MovEstoqueDTO> atualizar(@PathVariable Long id, @RequestBody MovEstoqueDTO dto) {
        MovEstoqueDTO atualizado = movEstoqueService.atualizarPorAdmFuncionario(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    // Deletar movimentação (reverte estoque antes de excluir)
    @DeleteMapping("/deletar/{id}")
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncAdm(authentication)")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        movEstoqueService.deletarMovEstoque(id);
        return ResponseEntity.noContent().build();
    }
}
