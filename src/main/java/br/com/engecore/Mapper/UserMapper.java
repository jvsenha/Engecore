package br.com.engecore.Mapper;

import br.com.engecore.DTO.ClienteDTO;
import br.com.engecore.DTO.FuncionarioDTO;
import br.com.engecore.DTO.UserDTO;
import br.com.engecore.Entity.ClienteEntity;
import br.com.engecore.Entity.FuncionarioEntity;
import br.com.engecore.Entity.UserEntity;

import java.util.stream.Collectors;


public class UserMapper {

    public static UserDTO toUserDTO(UserEntity user) {
        if (user == null) return null;

        UserDTO dto = new UserDTO();
        dto.setIdUsuario(user.getIdUsuario());
        dto.setNome(user.getNome());
        dto.setEmail(user.getEmail());
        dto.setTelefone(user.getTelefone());
        dto.setStatus(user.getStatus());
        dto.setRole(user.getRole());
        return dto;
    }

    // FuncionarioEntity → FuncionarioDTO
    public static FuncionarioDTO toFuncionarioDTO(FuncionarioEntity func) {
        if (func == null) return null;

        FuncionarioDTO dto = new FuncionarioDTO();
        dto.setIdUsuario(func.getIdUsuario());
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

    // ClienteEntity → ClienteDTO
    public static ClienteDTO toClienteDTO(ClienteEntity cliente) {
        if (cliente == null) return null;

        ClienteDTO dto = new ClienteDTO();
        dto.setIdUsuario(cliente.getIdUsuario());
        dto.setNome(cliente.getNome());
        dto.setEmail(cliente.getEmail());
        dto.setTelefone(cliente.getTelefone());
        dto.setStatus(cliente.getStatus());
        dto.setRole(cliente.getRole());

        dto.setCpf(cliente.getCpfCnpj());
        dto.setTipoPessoa(cliente.getTipoPessoa());
        dto.setEndereco(cliente.getEndereco());
        if (cliente.getObras() != null) {
            dto.setObrasIds(cliente.getObras()
                    .stream()
                    .map(o -> o.getIdObra())
                    .collect(Collectors.toList()));
        }

        return dto;
    }

}
