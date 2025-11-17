package br.com.engecore.Controller;

import br.com.engecore.DTO.ApiResponse;
import br.com.engecore.DTO.CotacaoDetalhesDTO;
import br.com.engecore.DTO.CotacaoListDTO;
import br.com.engecore.DTO.CotacaoRequestDTO;
import br.com.engecore.DTO.PropostaCotacaoDTO;
import br.com.engecore.Entity.CotacaoEntity;
import br.com.engecore.Service.CotacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cotacoes")
public class CotacaoController {

    @Autowired
    private CotacaoService cotacaoService;

    // =================================================================
    // OPERAÇÕES DE CRUD BÁSICAS
    // =================================================================

    /**
     * CREATE (Cotação e Propostas)
     * Recebe um DTO com uma *lista* de itens.
     */
    @PostMapping("/solicitar")
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public ResponseEntity<ApiResponse<List<CotacaoEntity>>> solicitarCotacao(@RequestBody CotacaoRequestDTO dto) {
        try {
            List<CotacaoEntity> cotacoes = cotacaoService.solicitarCotacao(dto);
            return ResponseEntity.ok(new ApiResponse<>(true, "Cotação(ões) solicitada(s) com sucesso", cotacoes));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    /**
     * READ (Listar Todos)
     * Retorna o DTO leve para a tabela.
     */
    @GetMapping("/listar")
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public ResponseEntity<ApiResponse<List<CotacaoListDTO>>> listarTodasCotacoes() {
        List<CotacaoListDTO> cotacoes = cotacaoService.listarTodas();
        return ResponseEntity.ok(new ApiResponse<>(true, "Cotações listadas com sucesso", cotacoes));
    }

    /**
     * READ (Detalhes por ID)
     * Retorna o DTO de detalhes.
     */
    @GetMapping("/{id}")
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public ResponseEntity<ApiResponse<CotacaoDetalhesDTO>> detalhesCotacao(@PathVariable Long id) {
        try {
            CotacaoDetalhesDTO cotacao = cotacaoService.detalhesCotacao(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Cotação encontrada", cotacao));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    /**
     * UPDATE (Atualizar Cotação)
     * (Ainda precisa de implementação da lógica de negócio no Service)
     */
    @PutMapping("/alterar/{id}")
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public ResponseEntity<ApiResponse<CotacaoDetalhesDTO>> atualizarCotacao(@PathVariable Long id, @RequestBody CotacaoRequestDTO dto) {
        try {
            CotacaoDetalhesDTO cotacaoAtualizada = cotacaoService.atualizarCotacao(id, dto);
            return ResponseEntity.ok(new ApiResponse<>(true, "Cotação atualizada com sucesso (LÓGICA PENDENTE)", cotacaoAtualizada));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    /**
     * DELETE (Deletar Cotação)
     */
    @DeleteMapping("/deletar/{id}")
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public ResponseEntity<ApiResponse<Void>> deletarCotacao(@PathVariable Long id) {
        try {
            cotacaoService.deletarCotacao(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Cotação deletada com sucesso", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // =================================================================
    // OPERAÇÕES DE PROPOSTA (FILHOS)
    // =================================================================

    /**
     * AÇÃO: Re-executa a busca automática de propostas para uma cotação existente.
     */
    @PostMapping("/{id}/regerar-propostas")
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public ResponseEntity<ApiResponse<List<PropostaCotacaoDTO>>> regerarPropostas(@PathVariable Long id) {
        try {
            List<PropostaCotacaoDTO> novasPropostas = cotacaoService.regerarPropostas(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Propostas atualizadas com sucesso", novasPropostas));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    /**
     * READ (Listar Propostas de uma Cotação)
     */
    @GetMapping("/{id}/propostas")
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication) or @securityService.isFornecedor(authentication)")
    public ResponseEntity<ApiResponse<List<PropostaCotacaoDTO>>> listarPropostas(@PathVariable Long id) {
        try {
            List<PropostaCotacaoDTO> propostas = cotacaoService.listarPropostas(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Propostas carregadas", propostas));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    /**
     * AÇÃO (Confirmar Proposta)
     */
    @PostMapping("/propostas/{propostaId}/confirmar")
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public ResponseEntity<ApiResponse<Void>> confirmarPedido(@PathVariable Long propostaId) {
        try {
            cotacaoService.confirmarPedido(propostaId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Pedido confirmado com sucesso", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}