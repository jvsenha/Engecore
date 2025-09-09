package br.com.engecore.Service;

import br.com.engecore.DTO.FuncionarioDTO;
import br.com.engecore.Entity.FuncionarioEntity;
import br.com.engecore.Entity.MovEstoqueEntity;
import br.com.engecore.Entity.MovFinanceiraEntity;
import br.com.engecore.Enum.Status;
import br.com.engecore.Mapper.UserMapper;
import br.com.engecore.Repository.FuncionarioRepository;
import br.com.engecore.Repository.MovEstoqueRepository;
import br.com.engecore.Repository.MovFinanceiraRepository;
import br.com.engecore.Util.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

public class FuncionarioService {


    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private MovFinanceiraRepository movFinanceiraRepository;
    @Autowired
    private MovEstoqueRepository movEstoqueRepository;

    @Autowired
    public PasswordEncoder passwordEncoder;

    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public FuncionarioDTO cadastrar(FuncionarioDTO dto) {
        FuncionarioEntity func = new FuncionarioEntity();

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

        return UserMapper.toFuncionarioDTO(func);
    }

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

        return UserMapper.toFuncionarioDTO(func);
    }

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

        return UserMapper.toFuncionarioDTO(func);
    }


    @PreAuthorize("@securityService.isAdmin(authentication)")
    public void deletarFuncionario(Long id) {
        funcionarioRepository.deleteById(id);
    }

    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public List<MovEstoqueEntity> historicoMovimentacoesEstoque(Long id) {
        return movEstoqueRepository.findByFuncionarioResponsavelIdUsuario(id);
    }

    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public List<MovFinanceiraEntity> historicoMovimentacoesFinanceira(Long id) {
        return movFinanceiraRepository.findByFuncionarioResponsavelIdUsuario(id);
    }

    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public FuncionarioDTO buscarFuncionario(Long id) {
        FuncionarioEntity funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funcionario não encontrado"));

        return UserMapper.toFuncionarioDTO(funcionario);
    }

    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public List<FuncionarioEntity> listar() {
        return funcionarioRepository.findAll();
    }

}
