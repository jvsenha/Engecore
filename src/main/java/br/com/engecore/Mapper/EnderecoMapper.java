package br.com.engecore.Mapper;

import br.com.engecore.DTO.EnderecoDTO;
import br.com.engecore.Entity.EnderecoEmbeddable;

public class EnderecoMapper {
    public static EnderecoDTO toEnderecoDTO(EnderecoEmbeddable endereco) {
        if (endereco == null) return null;

        return new EnderecoDTO(
                endereco.getRua(),
                endereco.getNumero(),
                endereco.getCidade(),
                endereco.getEstado(),
                endereco.getComplemento(),
                endereco.getBairro(),
                endereco.getCep()
        );
    }

    public static EnderecoEmbeddable toEnderecoEmbeddable(EnderecoDTO dto) {
        if (dto == null) return null;

        return new EnderecoEmbeddable(
                dto.getRua(),
                dto.getNumero(),
                dto.getCidade(),
                dto.getEstado(),
                dto.getComplemento(),
                dto.getBairro(),
                dto.getCep()
        );
    }
}
