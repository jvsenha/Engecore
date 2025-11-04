package br.com.engecore.Controller;

import br.com.engecore.DTO.*;
import br.com.engecore.Entity.ObrasEntity;
import br.com.engecore.Repository.ObrasRepository;
import br.com.engecore.Service.PdfService;
import br.com.engecore.Service.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/relatorios")
@PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    @Autowired
    private PdfService pdfService;

    // Repositório necessário para buscar o nome da obra para o relatório de fases
    @Autowired
    private ObrasRepository obrasRepository;

    // ===================================================================================
    // ENDPOINTS DE DADOS (JSON)
    // ===================================================================================

    @GetMapping("/obra/{id}/dre")
    public ResponseEntity<ApiResponse<DreObraDTO>> getDreDaObra(@PathVariable Long id) {
        try {
            DreObraDTO dto = relatorioService.getDrePorObra(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Relatório DRE da obra gerado com sucesso", dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/estoque/critico")
    public ResponseEntity<ApiResponse<List<MaterialEstoqueResponse>>> getEstoqueCritico() {
        try {
            List<MaterialEstoqueResponse> dtos = relatorioService.getEstoqueCritico();
            return ResponseEntity.ok(new ApiResponse<>(true, "Relatório de estoque crítico gerado com sucesso", dtos));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/insumos/curva-abc")
    public ResponseEntity<ApiResponse<List<CurvaAbcDTO>>> getCurvaAbcInsumos() {
        try {
            List<CurvaAbcDTO> dtos = relatorioService.getCurvaABCInsumos();
            return ResponseEntity.ok(new ApiResponse<>(true, "Relatório Curva ABC de Insumos gerado com sucesso", dtos));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/obra/{id}/fases-acompanhamento")
    public ResponseEntity<ApiResponse<List<AcompanhamentoFaseDTO>>> getAcompanhamentoFases(@PathVariable Long id) {
        try {
            List<AcompanhamentoFaseDTO> dtos = relatorioService.getAcompanhamentoFases(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Relatório de acompanhamento de fases gerado", dtos));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // ===================================================================================
    // ENDPOINTS DE GERAÇÃO DE PDF (HTML -> PDF)
    // ===================================================================================

    @GetMapping("/obra/{id}/dre/pdf")
    public ResponseEntity<byte[]> gerarPdfDreObra(@PathVariable Long id) {
        try {
            DreObraDTO dto = relatorioService.getDrePorObra(id);
            byte[] pdfBytes = pdfService.gerarPdfDreHtml(dto);
            return criarResponsePdf(pdfBytes, "DRE_Obra_" + id + ".pdf");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/estoque/critico/pdf")
    public ResponseEntity<byte[]> gerarPdfEstoqueCritico() {
        try {
            List<MaterialEstoqueResponse> dtos = relatorioService.getEstoqueCritico();
            byte[] pdfBytes = pdfService.gerarPdfEstoqueCriticoHtml(dtos);
            return criarResponsePdf(pdfBytes, "Relatorio_Estoque_Critico.pdf");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/insumos/curva-abc/pdf")
    public ResponseEntity<byte[]> gerarPdfCurvaAbcInsumos() {
        try {
            List<CurvaAbcDTO> dtos = relatorioService.getCurvaABCInsumos();
            byte[] pdfBytes = pdfService.gerarPdfCurvaAbcHtml(dtos);
            return criarResponsePdf(pdfBytes, "Relatorio_Curva_ABC.pdf");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/obra/{id}/fases-acompanhamento/pdf")
    public ResponseEntity<byte[]> gerarPdfAcompanhamentoFases(@PathVariable Long id) {
        try {
            // Este relatório precisa de dados de duas fontes
            ObrasEntity obra = obrasRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Obra não encontrada: " + id));
            List<AcompanhamentoFaseDTO> dtos = relatorioService.getAcompanhamentoFases(id);

            byte[] pdfBytes = pdfService.gerarPdfAcompanhamentoFasesHtml(dtos, obra.getNomeObra());
            return criarResponsePdf(pdfBytes, "Acompanhamento_Fases_Obra_" + id + ".pdf");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Método auxiliar para criar a resposta HTTP para um PDF.
     */
    private ResponseEntity<byte[]> criarResponsePdf(byte[] pdfBytes, String filename) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", filename); // Força o download
        // headers.setContentDispositionFormData("inline", filename); // Tenta abrir no navegador
        headers.setContentLength(pdfBytes.length);
        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}