package br.com.engecore.Service;

import br.com.engecore.DTO.FuncionarioDTO;
import br.com.engecore.Entity.FuncionarioEntity;
import br.com.engecore.Entity.MovimentacaoEstoqueEntity;
import br.com.engecore.Entity.MovimentacaoFinanceiraEntity;
import br.com.engecore.Enum.Status;
import br.com.engecore.Mapper.UserMapper;
import br.com.engecore.Repository.FuncionarioRepository;
import br.com.engecore.Repository.MovimentacaoEstoqueRepository;
import br.com.engecore.Repository.MovimentacaoFinanceiraRepository;
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
    private MovimentacaoFinanceiraRepository movimentacaoFinanceiraRepository;
    @Autowired
    private MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;

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
    public List<MovimentacaoEstoqueEntity> historicoMovimentacoesEstoque(Long id) {
        return movimentacaoEstoqueRepository.findByFuncionarioResponsavel(id);
    }

    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public List<MovimentacaoFinanceiraEntity> historicoMovimentacoesFinanceira(Long id) {
        return movimentacaoFinanceiraRepository.findByFuncionarioResponsavel(id);
    }
}
