package br.com.engecore.Service;

import br.com.engecore.DTO.ClienteDTO;
import br.com.engecore.Entity.ClienteEntity;
import br.com.engecore.Entity.MovimentacaoFinanceiraEntity;
import br.com.engecore.Entity.ObrasEntity;
import br.com.engecore.Enum.Status;
import br.com.engecore.Enum.TipoMovFinanceiro;
import br.com.engecore.Mapper.UserMapper;
import br.com.engecore.Repository.ClienteRepository;
import br.com.engecore.Repository.MovimentacaoFinanceiraRepository;
import br.com.engecore.Repository.ObrasRepository;
import br.com.engecore.Util.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ObrasRepository obrasRepository;

    @Autowired
    private MovimentacaoFinanceiraRepository movimentacaoFinanceiraRepository;

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    // Cadastro de cliente (feito por ADM ou FUNC)
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public ClienteDTO cadastrar(ClienteDTO dto) {
        ClienteEntity cliente = new ClienteEntity();

        cliente.setNome(dto.getNome());
        cliente.setEmail(dto.getEmail());
        cliente.setSenha(passwordEncoder.encode(dto.getSenha()));
        cliente.setTelefone(dto.getTelefone());
        cliente.setStatus(dto.getStatus() != null ? dto.getStatus() : Status.STATUS_ATIVO);
        cliente.setRole(dto.getRole());

        cliente.setTipoPessoa(dto.getTipoPessoa());
        cliente.setCpfCnpj(dto.getCpfCnpj());
        cliente.setEndereco(dto.getEndereco());

        if (dto.getObrasIds() != null) {
            List<ObrasEntity> obras = obrasRepository.findAllById(dto.getObrasIds());
            cliente.setObras(obras);
        }

        // Validar CPF/CNPJ antes de salvar
        if (!cliente.isCpfCnpjValido()) {
            throw new RuntimeException("CPF ou CNPJ inválido");
        }

        clienteRepository.save(cliente);
        return UserMapper.toClienteDTO(cliente);
    }

    // Atualização feita por ADM ou FUNC para qualquer cliente (precisa do ID)
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public ClienteDTO atualizarPorAdmFuncionario(Long id, ClienteDTO dto) {
        ClienteEntity cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        cliente.setNome(dto.getNome());
        cliente.setEmail(dto.getEmail());
        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            cliente.setSenha(passwordEncoder.encode(dto.getSenha()));
        }
        cliente.setTelefone(dto.getTelefone());
        cliente.setStatus(dto.getStatus() != null ? dto.getStatus() : Status.STATUS_ATIVO);
        cliente.setRole(dto.getRole());

        cliente.setTipoPessoa(dto.getTipoPessoa());
        cliente.setCpfCnpj(dto.getCpfCnpj());
        cliente.setEndereco(dto.getEndereco());

        if (dto.getObrasIds() != null) {
            List<ObrasEntity> obras = obrasRepository.findAllById(dto.getObrasIds());
            cliente.setObras(obras);
        }

        if (!cliente.isCpfCnpjValido()) {
            throw new RuntimeException("CPF ou CNPJ inválido");
        }

        clienteRepository.save(cliente);
        return UserMapper.toClienteDTO(cliente);
    }

    // Atualização de perfil do próprio cliente (usa ID do token)
    @PreAuthorize("@securityService.isCliente(authentication)")
    public ClienteDTO atualizarPerfilCliente(ClienteDTO dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        JwtAuthenticationFilter.JwtAuthenticationDetails details =
                (JwtAuthenticationFilter.JwtAuthenticationDetails) auth.getDetails();
        Long userId = details.getUserId();


        ClienteEntity cliente = clienteRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        // Campos permitidos para atualização pelo próprio cliente
        cliente.setNome(dto.getNome());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefone(dto.getTelefone());
        cliente.setEndereco(dto.getEndereco());

        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            cliente.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        clienteRepository.save(cliente);
        return UserMapper.toClienteDTO(cliente);
    }

    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public ClienteDTO buscarCliente(Long id) {
        ClienteEntity cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        return UserMapper.toClienteDTO(cliente);
    }

    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncAdm(authentication)")
    public void deletarCliente(Long id) {
        clienteRepository.deleteById(id);
    }


    public List<ObrasEntity> listarObras(Long id){
        return obrasRepository.findByCliente(id);
    }

    public BigDecimal calcularTotalGastoCliente(Long id){
        ClienteEntity cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado!"));

        BigDecimal totalGasto = BigDecimal.ZERO;

        for (ObrasEntity obra : cliente.getObras()) {
            List<MovimentacaoFinanceiraEntity> movimentacoes =
                    movimentacaoFinanceiraRepository.findByObra(obra.getIdObra());

            // Somar todas as despesas
            BigDecimal somaObra = movimentacoes.stream()
                    .filter(mov -> mov.getTipo() == TipoMovFinanceiro.DESPESA) // apenas despesas
                    .map(MovimentacaoFinanceiraEntity::getValor)               // pegar valor
                    .reduce(BigDecimal.ZERO, BigDecimal::add);                // somar

            totalGasto = totalGasto.add(somaObra);
        }

        return totalGasto;
    }

}
