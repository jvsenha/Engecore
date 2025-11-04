package br.com.engecore.Controller;

import br.com.engecore.DTO.ApiResponse;
import br.com.engecore.DTO.CotacaoRequestDTO;
import br.com.engecore.DTO.PropostaCotacaoDTO;
import br.com.engecore.Entity.CotacaoEntity;
import br.com.engecore.Service.CotacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cotacoes")
public class CotacaoController {

    @Autowired
    private CotacaoService cotacaoService;

    // Endpoint para a tela CotacaoDeValores.jsx (Botão "Gerar Cotação")
    @PostMapping("/solicitar")
    public ResponseEntity<ApiResponse<CotacaoEntity>> solicitarCotacao(@RequestBody CotacaoRequestDTO dto) {
        try {
            CotacaoEntity cotacao = cotacaoService.solicitarCotacao(dto);
            return ResponseEntity.ok(new ApiResponse<>(true, "Cotação solicitada com sucesso", cotacao));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // Endpoint para a tela MostrarCotacao.jsx (Carregar a tabela de comparação)
    @GetMapping("/{id}/propostas")
    public ResponseEntity<ApiResponse<List<PropostaCotacaoDTO>>> listarPropostas(@PathVariable Long id) {
        try {
            List<PropostaCotacaoDTO> propostas = cotacaoService.listarPropostas(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Propostas carregadas", propostas));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // Endpoint para a tela MostrarCotacao.jsx (Botão "Confirmar Pedido")
    @PostMapping("/propostas/{propostaId}/confirmar")
    public ResponseEntity<ApiResponse<Void>> confirmarPedido(@PathVariable Long propostaId) {
        try {
            cotacaoService.confirmarPedido(propostaId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Pedido confirmado com sucesso", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}