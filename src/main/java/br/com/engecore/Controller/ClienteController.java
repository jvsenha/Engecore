package br.com.engecore.Controller;

import br.com.engecore.DTO.ApiResponse;
import br.com.engecore.DTO.ClienteDTO;
import br.com.engecore.Entity.ObrasEntity;
import br.com.engecore.Service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/cliente")
public class ClienteController {
    @Autowired
    private ClienteService clienteService;

    @PostMapping("/cadastrar")
    public ResponseEntity<ApiResponse<ClienteDTO>> cadastrarCliente(@RequestBody ClienteDTO dto) {
        ClienteDTO cliente = clienteService.cadastrar(dto);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Cliente cadastrado com sucesso", cliente)
        );
    }

    @PutMapping("/alterar/{id}")
    public ResponseEntity<ApiResponse<ClienteDTO>> atualizarCliente(
            @PathVariable Long id,
            @RequestBody ClienteDTO dto
    ) {
        ClienteDTO clienteAtualizado = clienteService.atualizarPorAdmFuncionario(id, dto);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Cliente atualizado com sucesso", clienteAtualizado)
        );
    }

    @PutMapping("/alterar/perfil")
    public ResponseEntity<ApiResponse<ClienteDTO>> atualizarPerfil(@RequestBody ClienteDTO dto) {
        ClienteDTO clienteAtualizado = clienteService.atualizarPerfilCliente(dto);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Perfil atualizado com sucesso", clienteAtualizado)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClienteDTO>> buscarCliente(@PathVariable Long id) {
        ClienteDTO cliente = clienteService.buscarCliente(id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Cliente encontrado", cliente)
        );
    }

    @GetMapping("/listar")
    public ResponseEntity<ApiResponse<List<ClienteDTO>>> listar() {
        List<ClienteDTO> clientes = clienteService.listar();
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Cliente encontrado", clientes)
        );
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<ApiResponse<Void>> deletarCliente(@PathVariable Long id) {
        clienteService.deletarCliente(id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Cliente deletado com sucesso", null)
        );
    }

    @GetMapping("/{id}/obras")
    public ResponseEntity<ApiResponse<List<ObrasEntity>>> listarObras(@PathVariable Long id) {
        List<ObrasEntity> obras = clienteService.listarObras(id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Obras do cliente listadas com sucesso", obras)
        );
    }

    @GetMapping("/{id}/total-gasto")
    public ResponseEntity<ApiResponse<BigDecimal>> calcularTotalGasto(@PathVariable Long id) {
        BigDecimal total = clienteService.calcularTotalGastoCliente(id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Total gasto do cliente calculado", total)
        );
    }
}
