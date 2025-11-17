package br.com.engecore.Controller;

import br.com.engecore.DTO.ApiResponse;
import br.com.engecore.DTO.FornecedorDTO;
import br.com.engecore.Service.FornecedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fornecedor")
public class FornecedorController {

    @Autowired
    private FornecedorService fornecedorService;

    @PostMapping("/cadastrar")
    public ResponseEntity<ApiResponse<FornecedorDTO>> cadastrarFornecedor(@RequestBody FornecedorDTO dto) {
        FornecedorDTO fornecedor = fornecedorService.cadastrar(dto);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Fornecedor cadastrado com sucesso", fornecedor)
        );
    }

    @PutMapping("/alterar/{id}")
    public ResponseEntity<ApiResponse<FornecedorDTO>> atualizarFornecedor(
            @PathVariable Long id,
            @RequestBody FornecedorDTO dto
    ) {
        FornecedorDTO fornecedorAtualizado = fornecedorService.atualizarPorAdmFuncionario(id, dto);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Fornecedor atualizado com sucesso", fornecedorAtualizado)
        );
    }

    @PutMapping("/alterar/perfil")
    public ResponseEntity<ApiResponse<FornecedorDTO>> atualizarPerfil(@RequestBody FornecedorDTO dto) {
        FornecedorDTO fornecedorAtualizado = fornecedorService.atualizarPerfilFornecedor(dto);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Perfil atualizado com sucesso", fornecedorAtualizado)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FornecedorDTO>> buscarFornecedor(@PathVariable Long id) {
        FornecedorDTO fornecedor = fornecedorService.buscarFornecedor(id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Fornecedor encontrado", fornecedor)
        );
    }

    @GetMapping("/listar")
    public ResponseEntity<ApiResponse<List<FornecedorDTO>>> listar() {
        List<FornecedorDTO> fornecedores = fornecedorService.listar();
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Fornecedores encontrados", fornecedores)
        );
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<ApiResponse<Void>> deletarFornecedor(@PathVariable Long id) {
        fornecedorService.deletarFornecedor(id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Fornecedor deletado com sucesso", null)
        );
    }
}