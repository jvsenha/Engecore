package br.com.engecore.Controller;

import br.com.engecore.DTO.ApiResponse;
import br.com.engecore.DTO.ProdutoFornecedorDTO;
import br.com.engecore.Entity.ProdutoFornecedorEntity;
import br.com.engecore.Service.ProdutoFornecedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos-fornecedor")
public class ProdutoFornecedorController {

    @Autowired
    private ProdutoFornecedorService produtoFornecedorService;

    /**
     * Endpoint para cadastrar ou atualizar um produto no catálogo de um fornecedor.
     */
    @PostMapping("/cadastrar-atualizar")
    public ResponseEntity<ApiResponse<ProdutoFornecedorEntity>> cadastrarOuAtualizarProduto(
            @RequestBody ProdutoFornecedorDTO dto) {
        try {
            ProdutoFornecedorEntity produto = produtoFornecedorService.cadastrarOuAtualizar(dto);
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Produto salvo com sucesso", produto)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    /**
     * Endpoint para listar todos os produtos de um fornecedor específico.
     */
    @GetMapping("/listar/{fornecedorId}")
    public ResponseEntity<ApiResponse<List<ProdutoFornecedorEntity>>> listarProdutos(
            @PathVariable Long fornecedorId) {
        try {
            List<ProdutoFornecedorEntity> produtos = produtoFornecedorService.listarProdutosPorFornecedor(fornecedorId);
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Produtos listados com sucesso", produtos)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}