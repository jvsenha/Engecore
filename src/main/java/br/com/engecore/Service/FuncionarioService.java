package br.com.engecore.Service;

import br.com.engecore.DTO.FuncionarioDTO;
import br.com.engecore.DTO.FuncionarioResponse;
import br.com.engecore.Entity.*;
import br.com.engecore.Enum.Status;
import br.com.engecore.Enum.TipoPessoa;
import br.com.engecore.Mapper.UserMapper;
import br.com.engecore.Repository.*;
import br.com.engecore.Util.JwtAuthenticationFilter;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FuncionarioService {


    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private MovFinanceiraRepository movFinanceiraRepository;

    @Autowired
    private UsuarioFisicoRepository usuarioFisicoRepository;

    @Autowired
    private UsuarioJuridicoRepository usuarioJuridicoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovEstoqueRepository movEstoqueRepository;

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Autowired
    public UserMapper userMapper;

    @Transactional
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public FuncionarioDTO cadastrar(FuncionarioDTO dto) {
        FuncionarioEntity func = new FuncionarioEntity();

        func.setNome(dto.getNome());
        func.setEmail(dto.getEmail());
        func.setSenha(passwordEncoder.encode(dto.getSenha()));
        func.setTelefone(dto.getTelefone());
        func.setStatus(dto.getStatus() != null ? dto.getStatus() : Status.STATUS_ATIVO);
        func.setTipoPessoa(dto.getTipoPessoa());
        func.setRole(dto.getRole());

        func.setCargo(dto.getCargo());
        func.setSalario(dto.getSalario());
        func.setDataAdmissao(dto.getDataAdmissao());

        funcionarioRepository.save(func);

        if (func.getTipoPessoa() == TipoPessoa.FISICA) {
            UsuarioFisico fisico = usuarioFisicoRepository.findById(func.getId())
                    .orElse(new UsuarioFisico());
            fisico.setUsuario(func);
            fisico.setCpf(dto.getCpf());
            fisico.setRg(dto.getRg());
            fisico.setDataNascimento(dto.getDataNascimento());
            usuarioFisicoRepository.save(fisico);
        } else if (func.getTipoPessoa() == TipoPessoa.JURIDICA) {
            UsuarioJuridico juridico = usuarioJuridicoRepository.findById(func.getId())
                    .orElse(new UsuarioJuridico());
            juridico.setUsuario(func);
            juridico.setCnpj(dto.getCnpj());
            juridico.setRazaoSocial(dto.getRazaoSocial());
            juridico.setNomeFantasia(dto.getNomeFantasia());
            juridico.setInscricaoEstadual(dto.getInscricaoEstadual());
            usuarioJuridicoRepository.save(juridico);
        }


        return userMapper.toFuncionarioDTO(func);
    }

    @Transactional
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public FuncionarioDTO atualizarPorAdmFuncionario(Long id, FuncionarioDTO dto) {
        FuncionarioEntity func = funcionarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("funcionario não encontrado"));
        ;

        func.setNome(dto.getNome());
        func.setEmail(dto.getEmail());
        func.setSenha(passwordEncoder.encode(dto.getSenha()));
        func.setTelefone(dto.getTelefone());
        func.setStatus(dto.getStatus() != null ? dto.getStatus() : Status.STATUS_ATIVO);
        func.setRole(dto.getRole());

        func.setCargo(dto.getCargo());
        func.setSalario(dto.getSalario());
        func.setDataAdmissao(dto.getDataAdmissao());

        funcionarioRepository.save(func);

        if (func.getTipoPessoa() == TipoPessoa.FISICA) {
            UsuarioFisico fisico = usuarioFisicoRepository.findById(func.getId())
                    .orElse(new UsuarioFisico());
            fisico.setUsuario(func);
            fisico.setCpf(dto.getCpf());
            fisico.setRg(dto.getRg());
            fisico.setDataNascimento(dto.getDataNascimento());
            usuarioFisicoRepository.save(fisico);
        } else if (func.getTipoPessoa() == TipoPessoa.JURIDICA) {
            UsuarioJuridico juridico = usuarioJuridicoRepository.findById(func.getId())
                    .orElse(new UsuarioJuridico());
            juridico.setUsuario(func);
            juridico.setCnpj(dto.getCnpj());
            juridico.setRazaoSocial(dto.getRazaoSocial());
            juridico.setNomeFantasia(dto.getNomeFantasia());
            juridico.setInscricaoEstadual(dto.getInscricaoEstadual());
            usuarioJuridicoRepository.save(juridico);
        }

        return userMapper.toFuncionarioDTO(func);
    }

    @Transactional
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public FuncionarioDTO atualizarPerfilFuncionario(FuncionarioDTO dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        JwtAuthenticationFilter.JwtAuthenticationDetails details =
                (JwtAuthenticationFilter.JwtAuthenticationDetails) auth.getDetails();
        Long userId = details.getUserId();

        FuncionarioEntity func = funcionarioRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("funcionario não encontrado"));
        ;

        func.setNome(dto.getNome());
        func.setEmail(dto.getEmail());
        func.setSenha(passwordEncoder.encode(dto.getSenha()));
        func.setTelefone(dto.getTelefone());
        func.setStatus(dto.getStatus() != null ? dto.getStatus() : Status.STATUS_ATIVO);
        func.setRole(dto.getRole());

        func.setCargo(dto.getCargo());
        func.setSalario(dto.getSalario());
        func.setDataAdmissao(dto.getDataAdmissao());

        funcionarioRepository.save(func);

        return userMapper.toFuncionarioDTO(func);
    }


    @Transactional
    @PreAuthorize("@securityService.isAdmin(authentication)")
    public void deletarFuncionario(Long id) {
        usuarioFisicoRepository.deleteById(id);
        usuarioJuridicoRepository.deleteById(id);
        funcionarioRepository.deleteById(id);
        userRepository.deleteById(id);
    }

    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public List<MovEstoqueEntity> historicoMovimentacoesEstoque(Long id) {
        return movEstoqueRepository.findByFuncionarioResponsavelId(id);
    }

    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public List<MovFinanceiraEntity> historicoMovimentacoesFinanceira(Long id) {
        return movFinanceiraRepository.findByFuncionarioResponsavelId(id);
    }

    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public FuncionarioDTO detalhesFuncionario(Long id) {
        FuncionarioEntity funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funcionario não encontrado"));

        return userMapper.toFuncionarioDTO(funcionario);
    }

    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public List<FuncionarioResponse> listar() {
         List<FuncionarioEntity> funcionarios = funcionarioRepository.findAll();
        return funcionarios.stream()
                .map(userMapper::toFuncionarioResponse)
                .toList();
    }

}
