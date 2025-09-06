package br.com.engecore.Service;

import br.com.engecore.DTO.LoginRequest;
import br.com.engecore.DTO.LoginResponse;
import br.com.engecore.DTO.UserDTO;
import br.com.engecore.Entity.UserEntity;
import br.com.engecore.Entity.UsuarioFisico;
import br.com.engecore.Entity.UsuarioJuridico;
import br.com.engecore.Enum.Role;
import br.com.engecore.Enum.Status;
import br.com.engecore.Enum.TipoPessoa;
import br.com.engecore.Mapper.UserMapper;
import br.com.engecore.Repository.UserRepository;
import br.com.engecore.Repository.UsuarioFisicoRepository;
import br.com.engecore.Repository.UsuarioJuridicoRepository;
import br.com.engecore.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UsuarioFisicoRepository usuarioFisicoRepository;

    @Autowired
    private UsuarioJuridicoRepository usuarioJuridicoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private br.com.engecore.Service.SecurityService securityService;

    public LoginResponse autenticar(LoginRequest loginRequest) {
        UserEntity user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário ou senha inválidos"));

        if (!passwordEncoder.matches(loginRequest.getSenha(), user.getSenha())) {
            throw new RuntimeException("Usuário ou senha inválidos");
        }

        if (user.getStatus() != Status.STATUS_ATIVO) {
            throw new RuntimeException("Usuário inativo");
        }

        String token = jwtUtil.generateToken(user);

        return new LoginResponse(
                token,
                user.getEmail(),
                user.getNome(),
                user.getRole().name(),
                user.getIdUsuario()
        );
    }

    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncAdm(authentication)")
    public UserDTO cadastrar(UserDTO dto) {
        UserEntity user = new UserEntity();
        user.setNome(dto.getNome());
        user.setEmail(dto.getEmail());
        user.setSenha(passwordEncoder.encode(dto.getSenha()));
        user.setTelefone(dto.getTelefone());
        user.setStatus(dto.getStatus() != null ? dto.getStatus() : Status.STATUS_ATIVO);
        user.setRole(dto.getRole());
        user.setTipoPessoa(dto.getTipoPessoa());

        userRepository.save(user);

        if (dto.getTipoPessoa() == TipoPessoa.FISICA && dto.getCpf() != null) {
            UsuarioFisico fisico = new UsuarioFisico();
            fisico.setUsuario(user);
            fisico.setCpf(dto.getCpf());
            fisico.setRg(dto.getRg());
            fisico.setDataNascimento(dto.getDataNascimento());
            usuarioFisicoRepository.save(fisico);
        } else if (dto.getTipoPessoa() == TipoPessoa.JURIDICA && dto.getCnpj() != null) {
            UsuarioJuridico juridico = new UsuarioJuridico();
            juridico.setUsuario(user);
            juridico.setCnpj(dto.getCnpj());
            juridico.setRazaoSocial(dto.getRazaoSocial());
            juridico.setNomeFantasia(dto.getNomeFantasia());
            juridico.setInscricaoEstadual(dto.getInscricaoEstadual());
            usuarioJuridicoRepository.save(juridico);
        }

        return UserMapper.toUserDTO(user);
    }

    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncAdm(authentication)")
    public UserDTO atualizar(Long id, UserDTO dto) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        user.setNome(dto.getNome());
        user.setEmail(dto.getEmail());
        if (dto.getSenha() != null && !dto.getSenha().isEmpty()) {
            user.setSenha(passwordEncoder.encode(dto.getSenha()));
        }
        user.setTelefone(dto.getTelefone());
        user.setStatus(dto.getStatus() != null ? dto.getStatus() : Status.STATUS_ATIVO);
        user.setRole(dto.getRole());
        user.setTipoPessoa(dto.getTipoPessoa());

        userRepository.save(user);

        if (dto.getTipoPessoa() == TipoPessoa.FISICA) {
            UsuarioFisico fisico = usuarioFisicoRepository.findById(user.getIdUsuario())
                    .orElse(new UsuarioFisico());
            fisico.setUsuario(user);
            fisico.setCpf(dto.getCpf());
            fisico.setRg(dto.getRg());
            fisico.setDataNascimento(dto.getDataNascimento());
            usuarioFisicoRepository.save(fisico);
        } else if (dto.getTipoPessoa() == TipoPessoa.JURIDICA) {
            UsuarioJuridico juridico = usuarioJuridicoRepository.findById(user.getIdUsuario())
                    .orElse(new UsuarioJuridico());
            juridico.setUsuario(user);
            juridico.setCnpj(dto.getCnpj());
            juridico.setRazaoSocial(dto.getRazaoSocial());
            juridico.setNomeFantasia(dto.getNomeFantasia());
            juridico.setInscricaoEstadual(dto.getInscricaoEstadual());
            usuarioJuridicoRepository.save(juridico);
        }

        return UserMapper.toUserDTO(user);
    }

    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncAdm(authentication)")
    public void alterarSenhaAdmFunc(Long id, String novaSenha) {
        UserEntity user = buscarUsuario(id);
        user.setSenha(passwordEncoder.encode(novaSenha));
        userRepository.save(user);
    }

    @PreAuthorize("@securityService.isAdmin(authentication)")
    public void deletarUsuario(Long id) {
        userRepository.deleteById(id);
    }

    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public UserEntity buscarUsuario(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public List<UserEntity> listarTodos() {
        return userRepository.findAll();
    }

    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public List<UserEntity> listarPorRole(Role role) {
        return userRepository.findByRole(role);
    }

    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public List<UserEntity> listarAtivos() {
        return userRepository.findByStatus(Status.STATUS_ATIVO);
    }

    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public List<UserEntity> listarInativos() {
        return userRepository.findByStatus(Status.STATUS_INATIVO);
    }

    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public Status alternarStatus(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (user.getStatus() == Status.STATUS_ATIVO) {
            user.setStatus(Status.STATUS_INATIVO);
        } else {
            user.setStatus(Status.STATUS_ATIVO);
        }

        userRepository.save(user);
        return user.getStatus();
    }
}
