package br.com.engecore.Service;

import br.com.engecore.DTO.ClienteDTO;
import br.com.engecore.Entity.*;
import br.com.engecore.Enum.Status;
import br.com.engecore.Enum.TipoMovFinanceiro;
import br.com.engecore.Enum.TipoPessoa;
import br.com.engecore.Mapper.UserMapper;
import br.com.engecore.Repository.*;
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
    private UsuarioFisicoRepository usuarioFisicoRepository;

    @Autowired
    private UsuarioJuridicoRepository usuarioJuridicoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private MovFinanceiraRepository movFinanceiraRepository;

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
        cliente.setEndereco(dto.getEndereco());

        if (dto.getObrasIds() != null) {
            List<ObrasEntity> obras = obrasRepository.findAllById(dto.getObrasIds());
            cliente.setObras(obras);
        }

        clienteRepository.save(cliente);

        if (dto.getTipoPessoa() == TipoPessoa.FISICA && dto.getCpf() != null) {
            UsuarioFisico fisico = new UsuarioFisico();
            fisico.setUsuario(cliente);
            fisico.setCpf(dto.getCpf());
            fisico.setRg(dto.getRg());
            fisico.setDataNascimento(dto.getDataNascimento());
            usuarioFisicoRepository.save(fisico);
        } else if (dto.getTipoPessoa() == TipoPessoa.JURIDICA && dto.getCnpj() != null) {
            UsuarioJuridico juridico = new UsuarioJuridico();
            juridico.setUsuario(cliente);
            juridico.setCnpj(dto.getCnpj());
            juridico.setRazaoSocial(dto.getRazaoSocial());
            juridico.setNomeFantasia(dto.getNomeFantasia());
            juridico.setInscricaoEstadual(dto.getInscricaoEstadual());
            usuarioJuridicoRepository.save(juridico);
        }

        return UserMapper.toClienteDTO(cliente);
    }

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
        cliente.setEndereco(dto.getEndereco());

        if (dto.getObrasIds() != null) {
            List<ObrasEntity> obras = obrasRepository.findAllById(dto.getObrasIds());
            cliente.setObras(obras);
        }

        clienteRepository.save(cliente);

        if (dto.getTipoPessoa() == TipoPessoa.FISICA) {
            UsuarioFisico fisico = usuarioFisicoRepository.findById(cliente.getIdUsuario())
                    .orElse(new UsuarioFisico());
            fisico.setUsuario(cliente);
            fisico.setCpf(dto.getCpf());
            fisico.setRg(dto.getRg());
            fisico.setDataNascimento(dto.getDataNascimento());
            usuarioFisicoRepository.save(fisico);
        } else if (dto.getTipoPessoa() == TipoPessoa.JURIDICA) {
            UsuarioJuridico juridico = usuarioJuridicoRepository.findById(cliente.getIdUsuario())
                    .orElse(new UsuarioJuridico());
            juridico.setUsuario(cliente);
            juridico.setCnpj(dto.getCnpj());
            juridico.setRazaoSocial(dto.getRazaoSocial());
            juridico.setNomeFantasia(dto.getNomeFantasia());
            juridico.setInscricaoEstadual(dto.getInscricaoEstadual());
            usuarioJuridicoRepository.save(juridico);
        }

        return UserMapper.toClienteDTO(cliente);
    }

    @PreAuthorize("@securityService.isCliente(authentication)")
    public ClienteDTO atualizarPerfilCliente(ClienteDTO dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((Long) auth.getPrincipal());

        ClienteEntity cliente = clienteRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        cliente.setNome(dto.getNome());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefone(dto.getTelefone());
        cliente.setEndereco(dto.getEndereco());

        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            cliente.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        clienteRepository.save(cliente);

        if (cliente.getTipoPessoa() == TipoPessoa.FISICA) {
            UsuarioFisico fisico = usuarioFisicoRepository.findById(cliente.getIdUsuario())
                    .orElse(new UsuarioFisico());
            fisico.setUsuario(cliente);
            fisico.setCpf(dto.getCpf());
            fisico.setRg(dto.getRg());
            fisico.setDataNascimento(dto.getDataNascimento());
            usuarioFisicoRepository.save(fisico);
        } else if (cliente.getTipoPessoa() == TipoPessoa.JURIDICA) {
            UsuarioJuridico juridico = usuarioJuridicoRepository.findById(cliente.getIdUsuario())
                    .orElse(new UsuarioJuridico());
            juridico.setUsuario(cliente);
            juridico.setCnpj(dto.getCnpj());
            juridico.setRazaoSocial(dto.getRazaoSocial());
            juridico.setNomeFantasia(dto.getNomeFantasia());
            juridico.setInscricaoEstadual(dto.getInscricaoEstadual());
            usuarioJuridicoRepository.save(juridico);
        }

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

    public List<ObrasEntity> listarObras(Long id) {
        return obrasRepository.findByClienteIdUsuario(id);
    }

    public List<ClienteEntity> listar() {
        return clienteRepository.findAll();
    }

    public BigDecimal calcularTotalGastoCliente(Long id) {
        ClienteEntity cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado!"));

        BigDecimal totalGasto = BigDecimal.ZERO;

        for (ObrasEntity obra : cliente.getObras()) {
            List<MovFinanceiraEntity> movimentacoes =
                    movFinanceiraRepository.findByObraIdObra(obra.getIdObra());

            BigDecimal somaObra = movimentacoes.stream()
                    .filter(mov -> mov.getTipo() == TipoMovFinanceiro.DESPESA)
                    .map(MovFinanceiraEntity::getValor)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            totalGasto = totalGasto.add(somaObra);
        }

        return totalGasto;
    }
}
