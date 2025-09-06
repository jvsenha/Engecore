package br.com.engecore.Service;

import br.com.engecore.DTO.EstoqueMaterialRequest;
import br.com.engecore.DTO.EstoqueMaterialResponse;
import br.com.engecore.Entity.EstoqueMaterial;
import br.com.engecore.Mapper.EstoqueMaterialMapper;
import br.com.engecore.Repository.EstoqueMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public class EstoqueMaterialService {

    @Autowired
    private EstoqueMaterialRepository estoqueMaterialRepository;


    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public EstoqueMaterialResponse cadastrar(EstoqueMaterialRequest dto) {
        EstoqueMaterial estoqueMaterial = new EstoqueMaterial();

        estoqueMaterial.setMaterial(dto.getMaterial());
        estoqueMaterial.setEstoque(dto.getEstoque());
        estoqueMaterial.setQuantidadeMaxima(dto.getQuantidadeMaxima());
        estoqueMaterial.setQuantidadeMaxima(dto.getQuantidadeMinima());
        estoqueMaterial.setQuantidadeAtual(dto.getQuantidadeAtual());
        estoqueMaterialRepository.save(estoqueMaterial);
        return EstoqueMaterialMapper.toResponse(estoqueMaterial);
    }

    // Atualização feita por ADM ou FUNC para qualquer estoqueMaterial (precisa do ID)
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public EstoqueMaterialResponse atualizarPorAdmFuncionario(Long idEstoque, Long idMaterial, EstoqueMaterialRequest dto) {
        EstoqueMaterial estoqueMaterial = buscarMaterial(idEstoque, idMaterial);

        estoqueMaterial.setMaterial(dto.getMaterial());
        estoqueMaterial.setEstoque(dto.getEstoque());
        estoqueMaterial.setQuantidadeMaxima(dto.getQuantidadeMaxima());
        estoqueMaterial.setQuantidadeMaxima(dto.getQuantidadeMinima());
        estoqueMaterial.setQuantidadeAtual(dto.getQuantidadeAtual());
        estoqueMaterialRepository.save(estoqueMaterial);
        return EstoqueMaterialMapper.toResponse(estoqueMaterial);
    }

    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncAdm(authentication)")
    public void deletarCliente(Long id) {
        estoqueMaterialRepository.deleteById(id);
    }


    public List<EstoqueMaterial> listarMateriais(){
        return estoqueMaterialRepository.findAll();
    }

    public EstoqueMaterialResponse detalhesMaterial(Long idEstoque, Long idMaterial) {
        EstoqueMaterial estoqueMaterial = buscarMaterial(idEstoque, idMaterial);
        return EstoqueMaterialMapper.toResponse(estoqueMaterial);
    }


    @Scheduled(cron = "0 0 * * * *") // a cada 1h
    public void verificarEstoques() {
        List<EstoqueMaterial> estoques = estoqueMaterialRepository.findAll();
        estoques.stream()
                .filter(EstoqueMaterial::isEstoqueCritico)
                .forEach(em -> System.out.println("Estoque baixo: " + em.getMaterial().getNome()));
    }

    public void atualizarQuantidadeAtual(Long idEstoque, Long idMaterial, float novaQtd){
        EstoqueMaterial estoqueMaterial = buscarMaterial(idEstoque,idMaterial);
        estoqueMaterial.setQuantidadeAtual(novaQtd);
        estoqueMaterialRepository.save(estoqueMaterial);
    }

    public void atualizarQuantidadeMinima(Long idEstoque, Long idMaterial, float novaQtd){
        EstoqueMaterial estoqueMaterial = buscarMaterial(idEstoque,idMaterial);
        estoqueMaterial.setQuantidadeMinima(novaQtd);
        estoqueMaterialRepository.save(estoqueMaterial);
    }

    public EstoqueMaterial buscarMaterial(Long idEstoque, Long idMaterial) {
        return estoqueMaterialRepository.findByEstoque_IdAndMaterial_Id(idEstoque,idMaterial).orElseThrow(() -> new RuntimeException("Material não encontrado no estoque informado"));
    }
}
