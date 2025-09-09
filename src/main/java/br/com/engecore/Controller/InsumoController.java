package br.com.engecore.Controller;

import br.com.engecore.DTO.ApiResponse;
import br.com.engecore.DTO.InsumoDTO;
import br.com.engecore.Entity.InsumoEntity;
import br.com.engecore.Service.InsumoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/insumo")
public class InsumoController {
    @Autowired
    private InsumoService insumoService;

    @PostMapping("/cadastrar")
    public ResponseEntity<ApiResponse<InsumoDTO>> cadastrarInsumo(@RequestBody InsumoDTO dto) {
        InsumoDTO insumo = insumoService.cadastrar(dto);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Insumo cadastrado com sucesso", insumo)
        );
    }

    @PutMapping("/alterar/{id}")
    public ResponseEntity<ApiResponse<InsumoDTO>> atualizarInsumo(
            @PathVariable Long id,
            @RequestBody InsumoDTO dto
    ) {
        InsumoDTO insumoAtualizado = insumoService.atualizarPorAdmFuncionario(id, dto);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Insumo atualizado com sucesso", insumoAtualizado)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InsumoDTO>> detalheInsumo(@PathVariable Long id) {
        InsumoDTO insumo = insumoService.detalhesInsumo(id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Insumo encontrado", insumo)
        );
    }
    @GetMapping("/listar")
    public ResponseEntity<ApiResponse<List<InsumoEntity>>> listar() {
        List<InsumoEntity> insumos = insumoService.listar();
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Insumo encontrado", insumos)
        );
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<ApiResponse<Void>> deletarInsumo(@PathVariable Long id) {
        insumoService.deletarInsumo(id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Insumo deletado com sucesso", null)
        );
    }

}
