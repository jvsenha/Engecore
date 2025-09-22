package br.com.engecore.Controller;

import br.com.engecore.DTO.*;
import br.com.engecore.Enum.FaixaRenda;
import br.com.engecore.Enum.ProgramaSocial;
import br.com.engecore.Enum.StatusConst;
import br.com.engecore.Enum.TipoObra;
import br.com.engecore.Service.FasesService;
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


    // =========================== FASES ==========================//

    @Autowired
    private FasesService fasesService;

    @PostMapping("/fases/cadastrar")
    public ResponseEntity<ApiResponse<FasesDTO>> cadastrarFases(@RequestBody FasesDTO dto) {
        FasesDTO obra = fasesService.cadastrar(dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Fases cadastrado com sucesso", obra));
    }

    @PutMapping("/fases/alterar/{id}")
    public ResponseEntity<ApiResponse<FasesDTO>> atualizarPorAdmFuncionario(@PathVariable Long id, @RequestBody FasesDTO dto) {
        FasesDTO obraAtualizado = fasesService.atualizarPorAdmFuncionario(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Fases atualizada com sucesso!", obraAtualizado));
    }

    @GetMapping("/fases/{id}")
    public ResponseEntity<ApiResponse<FasesDTO>> detalheFases(@PathVariable Long id) {
        FasesDTO obra = fasesService.detalhesFases(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Fases encontrado", obra));
    }

    @GetMapping("/fases/listar")
    public ResponseEntity<ApiResponse<List<FasesResponse>>> listar() {
        List<FasesResponse> obras = fasesService.listarFases();
        return ResponseEntity.ok(new ApiResponse<>(true, "Fasess encontradas", obras));
    }

    @GetMapping("/fases/listar/{id}")
    public ResponseEntity<ApiResponse<List<FasesResponse>>> listarPorObra(@PathVariable Long id) {
        List<FasesResponse> obras = fasesService.listarFasesPorObra(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Fasess encontradas", obras));
    }

    @DeleteMapping("/fases/deletar/{id}")
    public ResponseEntity<ApiResponse<Void>> deletarFases(@PathVariable Long id) {
        fasesService.deletarFases(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Fases deletado com sucesso", null));
    }

}
