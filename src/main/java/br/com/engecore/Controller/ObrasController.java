package br.com.engecore.Controller;

import br.com.engecore.DTO.ApiResponse;
import br.com.engecore.DTO.ObrasDTO;
import br.com.engecore.DTO.UnidadeObrasRequest;
import br.com.engecore.Enum.FaixaRenda;
import br.com.engecore.Enum.ProgramaSocial;
import br.com.engecore.Enum.StatusConst;
import br.com.engecore.Enum.TipoObra;
import br.com.engecore.Service.ObrasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/obras")
public class ObrasController {
    @Autowired
    private ObrasService obrasService;

    @PostMapping("/cadastrar")
    public ResponseEntity<ApiResponse<ObrasDTO>> cadastrarObra(@RequestBody ObrasDTO dto) {
        ObrasDTO obra = obrasService.cadastrar(dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Obra cadastrado com sucesso", obra));
    }

    @PutMapping("/alterar/{id}")
    public ResponseEntity<ApiResponse<ObrasDTO>> atualizarPorAdmFuncionario(@PathVariable Long id, @RequestBody ObrasDTO dto) {
        ObrasDTO obraAtualizado = obrasService.atualizarPorAdmFuncionario(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Obra atualizada com sucesso!", obraAtualizado));
    }

    @PutMapping("/alterar/unidades/{id}")
    public ResponseEntity<ApiResponse<ObrasDTO>> atualizarUnidadesObra(@PathVariable Long id, @RequestBody UnidadeObrasRequest dto) {
        ObrasDTO obraAtualizado = obrasService.atualizarUnidades(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Obra atualizada com sucesso!", obraAtualizado));
    }

    @PutMapping("/validarDocumentos/{id}")
    public ResponseEntity<ApiResponse<ObrasDTO>> validarDocumentacao(@PathVariable Long id, @RequestBody Boolean status) {
        ObrasDTO obraAtualizado = obrasService.validarDocumentacao(id, status);
        return ResponseEntity.ok(new ApiResponse<>(true, "Obra atualizada com sucesso!", obraAtualizado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ObrasDTO>> detalheObra(@PathVariable Long id) {
        ObrasDTO obra = obrasService.detalhesObra(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Obra encontrado", obra));
    }

    @GetMapping("/listar")
    public ResponseEntity<ApiResponse<List<ObrasDTO>>> listar(@RequestParam Optional<StatusConst> status, @RequestParam Optional<Long> clienteId, @RequestParam Optional<Long> responsavelId, @RequestParam Optional<TipoObra> tipo, @RequestParam Optional<FaixaRenda> faixaRenda) {

        List<ObrasDTO> obras = obrasService.listar(status, clienteId, responsavelId, tipo, faixaRenda);
        return ResponseEntity.ok(new ApiResponse<>(true, "Obras encontradas", obras));
    }


    @PostMapping("/listar/programaSocial")
    public ResponseEntity<ApiResponse<List<ObrasDTO>>> listarprogramaSocial(@PathVariable ProgramaSocial programaSocial) {
        List<ObrasDTO> obras = obrasService.listarObrasPorPrograma(programaSocial);
        return ResponseEntity.ok(new ApiResponse<>(true, "Obra encontrado", obras));
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<ApiResponse<Void>> deletarObra(@PathVariable Long id) {
        obrasService.deletarObra(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Obra deletado com sucesso", null));
    }

}
