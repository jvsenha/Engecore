package br.com.engecore.Mapper;

import br.com.engecore.DTO.MaterialEstoqueRequest;
import br.com.engecore.DTO.MaterialEstoqueResponse;
import br.com.engecore.Entity.EstoqueEntity;
import br.com.engecore.Entity.InsumoEntity;
import br.com.engecore.Entity.MaterialEstoque;
import br.com.engecore.Repository.EstoqueRepository;
import br.com.engecore.Repository.InsumoRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MaterialEstoqueMapper {

    private final InsumoRepository insumoRepository;
    private final EstoqueRepository estoqueRepository;


    public MaterialEstoqueMapper(InsumoRepository insumoRepository, EstoqueRepository estoqueRepository) {
        this.insumoRepository = insumoRepository;
        this.estoqueRepository = estoqueRepository;
    }

    // DTO -> Entity
    public MaterialEstoque toEntity(MaterialEstoqueRequest request) {
        if (request == null) {
            return null;
        }

        MaterialEstoque entity = new MaterialEstoque();

        InsumoEntity insumo = insumoRepository.findById(request.getMaterialId())
                .orElseThrow(() -> new RuntimeException("Insumo não encontrado!"));

        EstoqueEntity estoque = estoqueRepository.findById(request.getEstoqueId())
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado!"));

        entity.setMaterial(insumo);
        entity.setEstoque(estoque);
        entity.setQuantidadeAtual(request.getQuantidadeAtual());
        entity.setQuantidadeMinima(request.getQuantidadeMinima());
        entity.setQuantidadeMaxima(request.getQuantidadeMaxima());

        return entity;
    }

    // Entity -> DTO Response
    public static MaterialEstoqueResponse toResponse(MaterialEstoque entity) {
        if (entity == null) {
            return null;
        }

        MaterialEstoqueResponse response = new MaterialEstoqueResponse();

        // IDs principais
        response.setMaterialEstoqueId(entity.getId());

        // Dados do Estoque
        if (entity.getEstoque() != null) {
            response.setEstoque(entity.getEstoque().getNome());
        }

        // Dados do Insumo
        if (entity.getMaterial() != null) {
            response.setInsumoId(entity.getMaterial().getId());
            response.setMaterial(entity.getMaterial().getNome());
            response.setUnidade(entity.getMaterial().getUnidade());
        }

        // Dados da Marca e Modelo (ESSENCIAIS PARA TRANSFERÊNCIA)
        if (entity.getMarca() != null) {
            response.setMarcaId(entity.getMarca().getId());
            response.setMarcaNome(entity.getMarca().getNome());
        }
        response.setModelo(entity.getModelo());
        response.setValor(entity.getValor());

        // Quantidades
        response.setQuantidadeAtual(entity.getQuantidadeAtual());
        response.setQuantidadeMinima(entity.getQuantidadeMinima());
        response.setQuantidadeMaxima(entity.getQuantidadeMaxima() != null ? entity.getQuantidadeMaxima() : BigDecimal.ZERO);
        response.setEstoqueCritico(entity.isEstoqueCritico());
        return response;
    }
}