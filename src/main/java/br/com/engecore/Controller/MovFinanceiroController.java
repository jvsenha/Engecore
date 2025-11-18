package br.com.engecore.Controller;

import br.com.engecore.DTO.ApiResponse;
import br.com.engecore.DTO.MovFinanceiraDTO;
import br.com.engecore.Entity.MovFinanceiraEntity;
import br.com.engecore.Service.MovFinanceiroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/movFinanceira")
public class MovFinanceiroController {

    @Autowired
    private MovFinanceiroService movFinanceiroService;

    @PostMapping("/cadastrar")
    public ResponseEntity<ApiResponse<MovFinanceiraDTO>> cadastrarMovimentacao(@RequestBody MovFinanceiraDTO dto) {
        MovFinanceiraDTO movimentacao = movFinanceiroService.cadastrar(dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Movimentação cadastrada com sucesso", movimentacao));
    }

    // --- NOVOS ENDPOINTS CRUD ---

    @PutMapping("/alterar/{id}")
    public ResponseEntity<ApiResponse<MovFinanceiraDTO>> atualizarMovimentacao(@PathVariable Long id, @RequestBody MovFinanceiraDTO dto) {
        MovFinanceiraDTO atualizada = movFinanceiroService.atualizar(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Movimentação atualizada com sucesso", atualizada));
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<ApiResponse<Void>> deletarMovimentacao(@PathVariable Long id) {
        movFinanceiroService.deletarMovFinanceira(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Movimentação deletada com sucesso", null));
    }

    // --- LISTAGENS ---

    @GetMapping("/listar")
    public ResponseEntity<ApiResponse<List<MovFinanceiraEntity>>> listarTodas() {
        List<MovFinanceiraEntity> movimentacoes = movFinanceiroService.listarMovFinanceira();
        return ResponseEntity.ok(new ApiResponse<>(true, "Todas as movimentações listadas", movimentacoes));
    }

    @GetMapping("/listar/obra/{idObra}")
    public ResponseEntity<ApiResponse<List<MovFinanceiraEntity>>> listarPorObra(@PathVariable Long idObra) {
        List<MovFinanceiraEntity> movimentacoes = movFinanceiroService.listarPorObra(idObra);
        return ResponseEntity.ok(new ApiResponse<>(true, "Movimentações da obra listadas", movimentacoes));
    }

    // CORREÇÃO: Removido o @PathVariable que causava erro
    @GetMapping("/listar/empresa")
    public ResponseEntity<ApiResponse<List<MovFinanceiraEntity>>> listarPelaEmpresa() {
        List<MovFinanceiraEntity> movimentacoes = movFinanceiroService.listarMovEmpresa();
        return ResponseEntity.ok(new ApiResponse<>(true, "Movimentações da Empresa listadas", movimentacoes));
    }

    @GetMapping("/listar/cliente/{idCliente}")
    public ResponseEntity<ApiResponse<List<MovFinanceiraDTO>>> listarPorCliente(@PathVariable Long idCliente) {
        List<MovFinanceiraDTO> movimentacoes = movFinanceiroService.listarPorCliente(idCliente);
        return ResponseEntity.ok(new ApiResponse<>(true, "Movimentações do cliente listadas", movimentacoes));
    }

    // --- SALDOS ---

    @GetMapping("/saldo/empresa")
    public ResponseEntity<ApiResponse<BigDecimal>> getSaldoEmpresa() {
        BigDecimal saldo = movFinanceiroService.calcularSaldoEmpresa();
        return ResponseEntity.ok(new ApiResponse<>(true, "Saldo da empresa calculado.", saldo));
    }

    @GetMapping("/saldo/obra/{idObra}")
    public ResponseEntity<ApiResponse<BigDecimal>> getSaldoObra(@PathVariable Long idObra) {
        BigDecimal saldo = movFinanceiroService.calcularSaldoObra(idObra);
        return ResponseEntity.ok(new ApiResponse<>(true, "Saldo da obra calculado.", saldo));
    }

    @GetMapping("/saldo/cliente/{idCliente}")
    public ResponseEntity<ApiResponse<BigDecimal>> getSaldoCliente(@PathVariable Long idCliente) {
        BigDecimal saldo = movFinanceiroService.calcularSaldoCliente(idCliente);
        return ResponseEntity.ok(new ApiResponse<>(true, "Saldo do cliente calculado.", saldo));
    }
}