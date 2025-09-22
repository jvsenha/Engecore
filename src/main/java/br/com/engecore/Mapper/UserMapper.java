package br.com.engecore.Mapper;

import br.com.engecore.DTO.ClienteDTO;
import br.com.engecore.DTO.FuncionarioDTO;
import br.com.engecore.DTO.FuncionarioResponse;
import br.com.engecore.DTO.UserDTO;
import br.com.engecore.Entity.*;
import br.com.engecore.Repository.UsuarioFisicoRepository;
import br.com.engecore.Repository.UsuarioJuridicoRepository;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {

    private final UsuarioFisicoRepository usuarioFisicoRepository;
    private final UsuarioJuridicoRepository usuarioJuridicoRepository;

    public UserMapper(UsuarioFisicoRepository usuarioFisicoRepository,
                      UsuarioJuridicoRepository usuarioJuridicoRepository) {
        this.usuarioFisicoRepository = usuarioFisicoRepository;
        this.usuarioJuridicoRepository = usuarioJuridicoRepository;
    }

    public static UserDTO toUserDTO(UserEntity user) {
        if (user == null) return null;

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setNome(user.getNome());
        dto.setEmail(user.getEmail());
        dto.setTelefone(user.getTelefone());
        dto.setStatus(user.getStatus());
        dto.setRole(user.getRole());
        dto.setTipoPessoa(user.getTipoPessoa());
        return dto;
    }

    // FuncionarioEntity → FuncionarioDTO
    public FuncionarioDTO toFuncionarioDTO(FuncionarioEntity func) {
        if (func == null) return null;

        FuncionarioDTO dto = new FuncionarioDTO();
        dto.setId(func.getId());
        dto.setNome(func.getNome());
        dto.setEmail(func.getEmail());
        dto.setTelefone(func.getTelefone());
        dto.setStatus(func.getStatus());
        dto.setRole(func.getRole());
        dto.setTipoPessoa(func.getTipoPessoa());
        dto.setCargo(func.getCargo());
        dto.setSalario(func.getSalario());
        dto.setDataAdmissao(func.getDataAdmissao());

        UsuarioFisico uf = usuarioFisicoRepository.findByUsuarioId(func.getId());
        if (uf != null) {
            dto.setCpf(uf.getCpf());
            dto.setRg(uf.getRg());
            dto.setDataNascimento(uf.getDataNascimento());
        }

        UsuarioJuridico uj = usuarioJuridicoRepository.findByUsuarioId(func.getId());
        if (uj != null) {
            dto.setCnpj(uj.getCnpj());
            dto.setRazaoSocial(uj.getRazaoSocial());
            dto.setNomeFantasia(uj.getNomeFantasia());
            dto.setInscricaoEstadual(uj.getInscricaoEstadual());
        }


        return dto;
    }

    // FuncionarioEntity → FuncionarioDTO
    public FuncionarioResponse toFuncionarioResponse(FuncionarioEntity func) {
        if (func == null) return null;

        FuncionarioResponse dto = new FuncionarioResponse();
        dto.setId(func.getId());
        dto.setNome(func.getNome());
        dto.setEmail(func.getEmail());
        dto.setTelefone(func.getTelefone());
        dto.setStatus(func.getStatus());
        dto.setRole(func.getRole());

        dto.setCargo(func.getCargo());
        dto.setSalario(func.getSalario());
        dto.setDataAdmissao(func.getDataAdmissao());
        return dto;
    }

    public ClienteDTO toClienteDTO(ClienteEntity cliente) {
        if (cliente == null) return null;

        ClienteDTO dto = new ClienteDTO();
        dto.setId(cliente.getId());
        dto.setNome(cliente.getNome());
        dto.setEmail(cliente.getEmail());
        dto.setTelefone(cliente.getTelefone());
        dto.setStatus(cliente.getStatus());
        dto.setRole(cliente.getRole());
        dto.setTipoPessoa(cliente.getTipoPessoa());
        dto.setEndereco(cliente.getEndereco());

        if (cliente.getObras() != null) {
            dto.setObrasIds(cliente.getObras().stream()
                    .map(ObrasEntity::getId)
                    .collect(Collectors.toList()));
        }

        UsuarioFisico uf = usuarioFisicoRepository.findByUsuarioId(cliente.getId());
        if (uf != null) {
            dto.setCpf(uf.getCpf());
            dto.setRg(uf.getRg());
            dto.setDataNascimento(uf.getDataNascimento());
        }

        UsuarioJuridico uj = usuarioJuridicoRepository.findByUsuarioId(cliente.getId());
        if (uj != null) {
            dto.setCnpj(uj.getCnpj());
            dto.setRazaoSocial(uj.getRazaoSocial());
            dto.setNomeFantasia(uj.getNomeFantasia());
            dto.setInscricaoEstadual(uj.getInscricaoEstadual());
        }

        return dto;
    }

}
