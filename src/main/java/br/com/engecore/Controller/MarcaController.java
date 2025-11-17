package br.com.engecore.Controller;

import br.com.engecore.DTO.ApiResponse;
import br.com.engecore.DTO.MarcaDTO;
import br.com.engecore.Entity.MarcaEntity;
import br.com.engecore.Service.MarcaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/marca")
public class MarcaController {

    @Autowired
    private MarcaService marcaService;

    @PostMapping("/cadastrar")
    public ResponseEntity<ApiResponse<MarcaDTO>> cadastrarMarca(@RequestBody MarcaDTO dto) {
        MarcaDTO marca = marcaService.cadastrar(dto);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Marca cadastrada com sucesso", marca)
        );
    }

    @PutMapping("/alterar/{id}")
    public ResponseEntity<ApiResponse<MarcaDTO>> atualizarMarca(
            @PathVariable Long id,
            @RequestBody MarcaDTO dto
    ) {
        MarcaDTO marcaAtualizada = marcaService.atualizar(id, dto);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Marca atualizada com sucesso", marcaAtualizada)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MarcaDTO>> detalheMarca(@PathVariable Long id) {
        MarcaDTO marca = marcaService.detalhesMarca(id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Marca encontrada", marca)
        );
    }

    @GetMapping("/listar")
    public ResponseEntity<ApiResponse<List<MarcaEntity>>> listar() {
        List<MarcaEntity> marcas = marcaService.listar();
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Marcas encontradas", marcas)
        );
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<ApiResponse<Void>> deletarMarca(@PathVariable Long id) {
        marcaService.deletarMarca(id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Marca deletada com sucesso", null)
        );
    }
}