package br.com.engecore.Service;

import br.com.engecore.DTO.InsumoDisponivelDTO;
import br.com.engecore.DTO.MaterialEstoqueRequest;
import br.com.engecore.DTO.MaterialEstoqueResponse;
import br.com.engecore.DTO.MaterialEstoqueUpdateDTO;
import br.com.engecore.Entity.EstoqueEntity;
import br.com.engecore.Entity.InsumoEntity;
import br.com.engecore.Entity.MarcaEntity;
import br.com.engecore.Entity.MaterialEstoque;
import br.com.engecore.Mapper.MaterialEstoqueMapper;
import br.com.engecore.Repository.EstoqueRepository;
import br.com.engecore.Repository.InsumoRepository;
import br.com.engecore.Repository.MaterialEstoqueRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaterialEstoqueService {
    @Autowired
    private MaterialEstoqueRepository materialEstoqueRepository;

    @Autowired
    private EstoqueRepository estoqueRepository;

    @Autowired
    private InsumoRepository insumoRepository;

    @Transactional
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public MaterialEstoqueResponse cadastrar(MaterialEstoqueRequest dto) {
        EstoqueEntity estoque = estoqueRepository.findById(dto.getEstoqueId())
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado"));
        InsumoEntity material = insumoRepository.findById(dto.getMaterialId())
                .orElseThrow(() -> new RuntimeException("Material não encontrado"));

        MaterialEstoque estoqueMaterial = new MaterialEstoque();
        estoqueMaterial.setEstoque(estoque);
        estoqueMaterial.setMaterial(material);
        estoqueMaterial.setValor(dto.getValor());
        estoqueMaterial.setQuantidadeAtual(dto.getQuantidadeAtual());
        estoqueMaterial.setQuantidadeMinima(dto.getQuantidadeMinima());
        estoqueMaterial.setQuantidadeMaxima(dto.getQuantidadeMaxima());

        materialEstoqueRepository.save(estoqueMaterial);
        return MaterialEstoqueMapper.toResponse(estoqueMaterial);
    }

    @Transactional
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public MaterialEstoqueResponse atualizarPorAdmFuncionario(Long id, MaterialEstoqueRequest dto) {
        MaterialEstoque estoqueMaterial = materialEstoqueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Material em estoque não encontrado"));

        EstoqueEntity estoque = estoqueRepository.findById(dto.getEstoqueId())
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado"));
        InsumoEntity material = insumoRepository.findById(dto.getMaterialId())
                .orElseThrow(() -> new RuntimeException("Material não encontrado"));

        estoqueMaterial.setEstoque(estoque);
        estoqueMaterial.setMaterial(material);
        estoqueMaterial.setValor(dto.getValor());
        estoqueMaterial.setQuantidadeAtual(dto.getQuantidadeAtual());
        estoqueMaterial.setQuantidadeMinima(dto.getQuantidadeMinima());
        estoqueMaterial.setQuantidadeMaxima(dto.getQuantidadeMaxima());

        materialEstoqueRepository.save(estoqueMaterial);
        return MaterialEstoqueMapper.toResponse(estoqueMaterial);
    }

    @Transactional
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public void deletar(Long id) {
        materialEstoqueRepository.deleteById(id);
    }

    public List<MaterialEstoque> listarMaterialEstoque() {
        return materialEstoqueRepository.findAll();
    }

    public List<InsumoDisponivelDTO> listarTodosInsumos() {
        return materialEstoqueRepository.findAll()
                .stream()
                .map(this::mapToInsumoDisponivelDTO)
                .toList();
    }


    public MaterialEstoqueResponse detalhesMaterialEstoque(Long id) {
        MaterialEstoque estoqueMaterial = materialEstoqueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Material em estoque não encontrado"));
        return MaterialEstoqueMapper.toResponse(estoqueMaterial);
    }

    @Scheduled(cron = "0 0 * * * *") // a cada 1h
    public void verificarEstoques() {
        List<MaterialEstoque> estoques = materialEstoqueRepository.findAll();
        estoques.stream()
                .filter(MaterialEstoque::isEstoqueCritico)
                .forEach(em -> System.out.println("Estoque baixo: " + em.getMaterial().getNome()));
    }


    public List<MaterialEstoqueResponse> listarPorEstoque(Long idEstoque) {
        List<MaterialEstoque> lista = materialEstoqueRepository.findByEstoqueId(idEstoque);
        return lista.stream().map(MaterialEstoqueMapper::toResponse).toList();
    }

    public List<InsumoDisponivelDTO> listarInsumosPorEstoque(Long estoqueId) {

        List<MaterialEstoque> materiaisNoEstoque = materialEstoqueRepository.findByEstoque_Id(estoqueId);


        return materiaisNoEstoque.stream()
                .map(this::mapToInsumoDisponivelDTO)
                .collect(Collectors.toList());
    }


    private InsumoDisponivelDTO mapToInsumoDisponivelDTO(MaterialEstoque material) {
        InsumoEntity insumo = material.getMaterial();
        MarcaEntity marca = material.getMarca();

        return new InsumoDisponivelDTO(
                material.getId(),
                insumo.getId(),
                insumo.getNome(),
                insumo.getUnidade(),
                material.getQuantidadeAtual(),
                material.getQuantidadeMinima(),
                material.getQuantidadeMaxima(),
                material.getValor(),
                marca != null ? marca.getId() : null,
                marca != null ? marca.getNome() : "Sem Marca",
                material.getModelo(),
                material.isEstoqueCritico()
        );
    }
    @Transactional
    public InsumoDisponivelDTO atualizarDados(Long materialEstoqueId, MaterialEstoqueUpdateDTO dto) {

        // 1. Encontra a entidade no banco
        MaterialEstoque materialEstoque = materialEstoqueRepository.findById(materialEstoqueId)
                .orElseThrow(() -> new EntityNotFoundException("Produto no estoque não encontrado com ID: " + materialEstoqueId));

        // 2. Atualiza os campos da entidade com os dados do DTO
        materialEstoque.setValor(dto.getValor());
        materialEstoque.setModelo(dto.getModelo());
        materialEstoque.setQuantidadeMinima(dto.getQuantidadeMinima());
        materialEstoque.setQuantidadeMaxima(dto.getQuantidadeMaxima());

        // 3. Salva a entidade (o @Transactional cuida do commit)
        MaterialEstoque salvo = materialEstoqueRepository.save(materialEstoque);

        // 4. Retorna o DTO de resposta atualizado (que o front-end espera)
        return mapToInsumoDisponivelDTO(salvo);
    }
}
