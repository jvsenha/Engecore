package br.com.engecore.Controller;

import br.com.engecore.DTO.ApiResponse;
import br.com.engecore.DTO.EstoqueDTO;
import br.com.engecore.DTO.EstoqueResponse;
import br.com.engecore.DTO.MaterialEstoqueResponse;
import br.com.engecore.Entity.EstoqueEntity;
import br.com.engecore.Entity.MaterialEstoque;
import br.com.engecore.Service.EstoqueService;
import br.com.engecore.Service.MaterialEstoqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estoque")
public class EstoqueController {

    @Autowired
    private EstoqueService estoqueService;

    @Autowired
    private MaterialEstoqueService materialEstoqueService;

    // Cadastrar estoque
    @PostMapping("/cadastrar")
    public ResponseEntity<ApiResponse<EstoqueResponse>> cadastrarEstoque(@RequestBody EstoqueDTO dto) {
        EstoqueResponse estoque = estoqueService.cadastrar(dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Estoque cadastrado com sucesso", estoque));
    }

    // Atualizar dados do estoque
    @PutMapping("/alterar/{id}")
    public ResponseEntity<ApiResponse<EstoqueResponse>> atualizarEstoque(@PathVariable Long id, @RequestBody EstoqueDTO dto) {
        EstoqueResponse estoqueAtualizado = estoqueService.atualizarPorAdmFuncionario(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Estoque atualizado com sucesso!", estoqueAtualizado));
    }

    // Listar todos os estoques
    @GetMapping("/listar")
    public ResponseEntity<ApiResponse<List<EstoqueEntity>>> listarEstoque() {
        List<EstoqueEntity> estoques = estoqueService.listar();
        return ResponseEntity.ok(new ApiResponse<>(true, "Estoques encontrados", estoques));
    }

    // Detalhar estoque específico
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EstoqueResponse>> detalheEstoque(@PathVariable Long id) {
        EstoqueResponse estoque = estoqueService.detalhesEstoque(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Estoque encontrado", estoque));
    }

    // Listar todos os materiais
    @GetMapping("/listar/materiais")
    public ResponseEntity<ApiResponse<List<MaterialEstoque>>> listarMaterialEstoque() {
        List<MaterialEstoque> materiais = materialEstoqueService.listarMaterialEstoque();
        return ResponseEntity.ok(new ApiResponse<>(true, "Materiais encontrados", materiais));
    }

    // Listar materiais por estoque
    @GetMapping("/listar/materiais/{id}")
    public ResponseEntity<ApiResponse<List<MaterialEstoqueResponse>>> listarMaterialPorEstoque(@PathVariable Long id) {
        List<MaterialEstoqueResponse> materiais = materialEstoqueService.listarPorEstoque(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Materiais encontrados", materiais));
    }

    // Detalhar material específico
    @GetMapping("/material/{id}")
    public ResponseEntity<ApiResponse<MaterialEstoqueResponse>> detalheMaterialEstoque(@PathVariable Long id) {
        MaterialEstoqueResponse material = materialEstoqueService.detalhesMaterialEstoque(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Material encontrado", material));
    }

    // Deletar estoque (somente se estiver vazio)
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<ApiResponse<Void>> deletarEstoque(@PathVariable Long id) {
        estoqueService.deletarEstoque(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Estoque deletado com sucesso", null));
    }
}
