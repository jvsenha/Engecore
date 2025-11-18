package br.com.engecore.Controller;

import br.com.engecore.DTO.ApiResponse;
import br.com.engecore.DTO.InsumoDisponivelDTO;
import br.com.engecore.DTO.MaterialEstoqueResponse;
import br.com.engecore.DTO.MaterialEstoqueUpdateDTO;
import br.com.engecore.Entity.MaterialEstoque;
import br.com.engecore.Service.MaterialEstoqueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/material-estoque") // Ou onde fizer sentido
@RequiredArgsConstructor
public class MaterialEstoqueController {

    private final MaterialEstoqueService materialEstoqueService;

    @GetMapping("/listar/por-estoque/{estoqueId}")
    public ResponseEntity<List<InsumoDisponivelDTO>> getInsumosPorEstoque(@PathVariable Long estoqueId) {
        List<InsumoDisponivelDTO> insumos = materialEstoqueService.listarInsumosPorEstoque(estoqueId);
        // Não precisa de ResponseDTO(true, "...", insumos) se não for o seu padrão
        return ResponseEntity.ok(insumos);
    }

    @PutMapping("/alterar/{id}")
    public ResponseEntity<ApiResponse<InsumoDisponivelDTO>> alterarMaterialEstoque(
            @PathVariable("id") Long id,
            @Valid @RequestBody MaterialEstoqueUpdateDTO dto) {

        InsumoDisponivelDTO atualizado = materialEstoqueService.atualizarDados(id, dto);

        return ResponseEntity.ok(new ApiResponse<>(true, "Produto de estoque atualizado com sucesso.", atualizado));
    }

    // --- NOVO ENDPOINT: DELETAR ---
    // (Bônus: Você também vai precisar disso para o botão de deletar)
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<ApiResponse<Void>> deletarMaterialEstoque(@PathVariable("id") Long id) {
        // (Você precisará criar o método 'deletarItemEstoque' no seu service)
        // materialEstoqueService.deletarItemEstoque(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Item deletado com sucesso", null));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<InsumoDisponivelDTO>> getInsumos() {
        List<InsumoDisponivelDTO> insumos = materialEstoqueService.listarTodosInsumos();
        return ResponseEntity.ok(insumos);
    }

}