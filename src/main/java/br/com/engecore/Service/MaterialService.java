package br.com.engecore.Service;

import br.com.engecore.DTO.MaterialDTO;
import br.com.engecore.Entity.MaterialEntity;
import br.com.engecore.Mapper.MaterialMapper;
import br.com.engecore.Repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public class MaterialService {

    @Autowired
    private MaterialRepository materialRepository;

    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public MaterialDTO cadastrar(MaterialDTO dto) {
        MaterialEntity material = new MaterialEntity();

        material.setNome(dto.getNome());
        material.setUnidade(dto.getUnidade());
        materialRepository.save(material);
        return MaterialMapper.toDTO(material);
    }

    // Atualização feita por ADM ou FUNC para qualquer material (precisa do ID)
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public MaterialDTO atualizarPorAdmFuncionario(Long id, MaterialDTO dto) {
        MaterialEntity material = buscarMaterial(id);

        material.setNome(dto.getNome());
        material.setUnidade(dto.getUnidade());

        materialRepository.save(material);
        return MaterialMapper.toDTO(material);
    }

    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncAdm(authentication)")
    public void deletarCliente(Long id) {
        materialRepository.deleteById(id);
    }


    public List<MaterialEntity> listarMateriais(){
        return materialRepository.findAll();
    }

    public MaterialDTO detalhesMaterial(Long id) {
        MaterialEntity material = materialRepository.findById(id).orElseThrow(() -> new RuntimeException("material não encontrada!"));
        return MaterialMapper.toDTO(material);
    }
    public MaterialEntity buscarMaterial(Long id) {
        return materialRepository.findById(id).orElseThrow(() -> new RuntimeException("material não encontrada"));
    }


}
