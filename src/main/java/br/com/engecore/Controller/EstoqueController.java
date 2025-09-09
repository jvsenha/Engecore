package br.com.engecore.Controller;

import br.com.engecore.DTO.ApiResponse;
import br.com.engecore.DTO.EstoqueDTO;
import br.com.engecore.Entity.EstoqueEntity;
import br.com.engecore.Service.EstoqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estoque")
public class EstoqueController {
    @Autowired
    private EstoqueService estoqueService;

    @PostMapping("/cadastrar")
    public ResponseEntity<ApiResponse<EstoqueDTO>> cadastrarestoque(@RequestBody EstoqueDTO dto) {
        EstoqueDTO estoque = estoqueService.cadastrar(dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "estoque cadastrado com sucesso", estoque));
    }

    @PutMapping("/alterar/{id}")
    public ResponseEntity<ApiResponse<EstoqueDTO>> atualizar(@PathVariable Long id, @RequestBody EstoqueDTO dto) {
        EstoqueDTO estoqueAtualizado = estoqueService.atualizarPorAdmFuncionario(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "estoque atualizada com sucesso!", estoqueAtualizado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EstoqueDTO>> detalheestoque(@PathVariable Long id) {
        EstoqueDTO estoque = estoqueService.detalhesEstoque(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "estoque encontrado", estoque));
    }

    @GetMapping("/listar")
    public ResponseEntity<ApiResponse<List<EstoqueEntity>>> listarEstoque() {
        List<EstoqueEntity> estoque = estoqueService.listar();
        return ResponseEntity.ok(new ApiResponse<>(true, "Estoque encontradas", estoque));
    }


    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<ApiResponse<Void>> deletarestoque(@PathVariable Long id) {
        estoqueService.deletarEstoque(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "estoque deletado com sucesso", null));
    }

}
