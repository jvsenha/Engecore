package br.com.engecore.Service;

import br.com.engecore.DTO.FornecedorDTO;
import br.com.engecore.Entity.FornecedorEntity;
import br.com.engecore.Entity.UsuarioFisico;
import br.com.engecore.Entity.UsuarioJuridico;
import br.com.engecore.Enum.Role;
import br.com.engecore.Enum.Status;
import br.com.engecore.Enum.TipoPessoa;
import br.com.engecore.Mapper.UserMapper;
import br.com.engecore.Repository.FornecedorRepository;
import br.com.engecore.Repository.UserRepository;
import br.com.engecore.Repository.UsuarioFisicoRepository;
import br.com.engecore.Repository.UsuarioJuridicoRepository;
import br.com.engecore.Util.JwtAuthenticationFilter;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FornecedorService {

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @Autowired
    private UsuarioFisicoRepository usuarioFisicoRepository;

    @Autowired
    private UsuarioJuridicoRepository usuarioJuridicoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Transactional
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public FornecedorDTO cadastrar(FornecedorDTO dto) {
        FornecedorEntity fornecedor = new FornecedorEntity();
        fornecedor.setNome(dto.getNome());
        fornecedor.setEmail(dto.getEmail());
        fornecedor.setSenha(passwordEncoder.encode(dto.getSenha()));
        fornecedor.setTelefone(dto.getTelefone());
        fornecedor.setStatus(dto.getStatus() != null ? dto.getStatus() : Status.STATUS_ATIVO);
        fornecedor.setRole(Role.ROLE_FORNECEDOR); // Travado em ROLE_FORNECEDOR
        fornecedor.setTipoPessoa(dto.getTipoPessoa());

        fornecedorRepository.save(fornecedor);

        // Salva dados PF/PJ
        salvarDadosPessoa(fornecedor, dto);

        return userMapper.toFornecedorDTO(fornecedor);
    }

    @Transactional
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public FornecedorDTO atualizarPorAdmFuncionario(Long id, FornecedorDTO dto) {
        FornecedorEntity fornecedor = fornecedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));

        fornecedor.setNome(dto.getNome());
        fornecedor.setEmail(dto.getEmail());
        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            fornecedor.setSenha(passwordEncoder.encode(dto.getSenha()));
        }
        fornecedor.setTelefone(dto.getTelefone());
        fornecedor.setStatus(dto.getStatus() != null ? dto.getStatus() : Status.STATUS_ATIVO);
        fornecedor.setRole(Role.ROLE_FORNECEDOR); // Travado
        fornecedor.setTipoPessoa(dto.getTipoPessoa());

        fornecedorRepository.save(fornecedor);

        // Atualiza dados PF/PJ
        salvarDadosPessoa(fornecedor, dto);

        return userMapper.toFornecedorDTO(fornecedor);
    }

    @Transactional
    @PreAuthorize("@securityService.isFornecedor(authentication)")
    public FornecedorDTO atualizarPerfilFornecedor(FornecedorDTO dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        JwtAuthenticationFilter.JwtAuthenticationDetails details =
                (JwtAuthenticationFilter.JwtAuthenticationDetails) auth.getDetails();
        Long userId = details.getUserId();

        FornecedorEntity fornecedor = fornecedorRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));

        // Campos que o próprio fornecedor pode alterar
        fornecedor.setNome(dto.getNome());
        fornecedor.setEmail(dto.getEmail());
        fornecedor.setTelefone(dto.getTelefone());

        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            fornecedor.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        fornecedorRepository.save(fornecedor);

        // Atualiza dados PF/PJ
        salvarDadosPessoa(fornecedor, dto);

        return userMapper.toFornecedorDTO(fornecedor);
    }

    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public FornecedorDTO buscarFornecedor(Long id) {
        FornecedorEntity fornecedor = fornecedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));

        return userMapper.toFornecedorDTO(fornecedor);
    }

    @Transactional
    @PreAuthorize("@securityService.isAdmin(authentication)")
    public void deletarFornecedor(Long id) {
        // Remove dependências de PF/PJ e depois o usuário
        usuarioFisicoRepository.deleteById(id);
        usuarioJuridicoRepository.deleteById(id);
        fornecedorRepository.deleteById(id);
        userRepository.deleteById(id);
    }

    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public List<FornecedorDTO> listar() {
        List<FornecedorEntity> fornecedores = fornecedorRepository.findAll();
        return fornecedores.stream()
                .map(userMapper::toFornecedorDTO)
                .collect(Collectors.toList());
    }

    // Método auxiliar privado para salvar PF/PJ
    private void salvarDadosPessoa(FornecedorEntity fornecedor, FornecedorDTO dto) {
        if (dto.getTipoPessoa() == TipoPessoa.FISICA && dto.getCpf() != null) {
            UsuarioFisico fisico = usuarioFisicoRepository.findById(fornecedor.getId())
                    .orElse(new UsuarioFisico());
            fisico.setUsuario(fornecedor);
            fisico.setCpf(dto.getCpf());
            fisico.setRg(dto.getRg());
            fisico.setDataNascimento(dto.getDataNascimento());
            usuarioFisicoRepository.save(fisico);
        } else if (dto.getTipoPessoa() == TipoPessoa.JURIDICA && dto.getCnpj() != null) {
            UsuarioJuridico juridico = usuarioJuridicoRepository.findById(fornecedor.getId())
                    .orElse(new UsuarioJuridico());
            juridico.setUsuario(fornecedor);
            juridico.setCnpj(dto.getCnpj());
            juridico.setRazaoSocial(dto.getRazaoSocial());
            juridico.setNomeFantasia(dto.getNomeFantasia());
            juridico.setInscricaoEstadual(dto.getInscricaoEstadual());
            usuarioJuridicoRepository.save(juridico);
        }
    }
}