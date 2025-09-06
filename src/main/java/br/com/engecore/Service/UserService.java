package br.com.engecore.Service;

import br.com.engecore.DTO.LoginRequest;
import br.com.engecore.DTO.LoginResponse;
import br.com.engecore.DTO.UserDTO;
import br.com.engecore.Entity.UserEntity;
import br.com.engecore.Enum.Role;
import br.com.engecore.Enum.Status;
import br.com.engecore.Mapper.UserMapper;
import br.com.engecore.Repository.UserRepository;
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
    public PasswordEncoder passwordEncoder;

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
            throw new RuntimeException("Usuário inativo"); // aqui pode manter específico
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

        userRepository.save(user);
        return UserMapper.toUserDTO(user);
    }

    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncAdm(authentication)")
    public UserDTO atualizar(Long id, UserDTO dto) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado"));

        user.setNome(dto.getNome());
        user.setEmail(dto.getEmail());
        user.setSenha(passwordEncoder.encode(dto.getSenha()));
        user.setTelefone(dto.getTelefone());
        user.setStatus(dto.getStatus() != null ? dto.getStatus() : Status.STATUS_ATIVO);
        user.setRole(dto.getRole());

        userRepository.save(user);
        return UserMapper.toUserDTO(user);
    }

    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncAdm(authentication)")
    public void AlterarSenhaAdmFunc(Long id, String novaSenha){
        UserEntity user = buscarUsuario(id);
        user.setSenha(passwordEncoder.encode(novaSenha));
    }

    @PreAuthorize("@securityService.isAdmin(authentication)")
    public void deletarUsuario(Long id) {
        userRepository.deleteById(id);
    }

    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public UserEntity buscarUsuario(Long id){
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado"));
        return user;
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
        UserEntity cliente = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        // Alterna entre ATIVO e INATIVO
        if (cliente.getStatus() == Status.STATUS_ATIVO) {
            cliente.setStatus(Status.STATUS_INATIVO);
        } else {
            cliente.setStatus(Status.STATUS_ATIVO);
        }

        userRepository.save(cliente);
        return cliente.getStatus();
    }


}
