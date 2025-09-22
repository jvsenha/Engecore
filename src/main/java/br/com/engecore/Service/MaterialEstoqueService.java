package br.com.engecore.Service;

import br.com.engecore.DTO.MaterialEstoqueRequest;
import br.com.engecore.DTO.MaterialEstoqueResponse;
import br.com.engecore.Entity.EstoqueEntity;
import br.com.engecore.Entity.InsumoEntity;
import br.com.engecore.Entity.MaterialEstoque;
import br.com.engecore.Mapper.MaterialEstoqueMapper;
import br.com.engecore.Repository.EstoqueRepository;
import br.com.engecore.Repository.InsumoRepository;
import br.com.engecore.Repository.MaterialEstoqueRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

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

    @Transactional
    public void atualizarQuantidadeAtual(Long id, BigDecimal novaQtd) {
        MaterialEstoque estoqueMaterial = materialEstoqueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Material em estoque não encontrado"));
        estoqueMaterial.setQuantidadeAtual(novaQtd);
        materialEstoqueRepository.save(estoqueMaterial);
    }

    @Transactional
    public void atualizarQuantidadeMinima(Long id, BigDecimal novaQtd) {
        MaterialEstoque estoqueMaterial = materialEstoqueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Material em estoque não encontrado"));
        estoqueMaterial.setQuantidadeMinima(novaQtd);
        materialEstoqueRepository.save(estoqueMaterial);
    }

    public List<MaterialEstoqueResponse> listarPorEstoque(Long idEstoque) {
        List<MaterialEstoque> lista = materialEstoqueRepository.findByEstoqueId(idEstoque);
        return lista.stream().map(MaterialEstoqueMapper::toResponse).toList();
    }
}
