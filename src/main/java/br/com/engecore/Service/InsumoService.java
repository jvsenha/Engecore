package br.com.engecore.Service;

import br.com.engecore.DTO.InsumoDTO;
import br.com.engecore.Entity.InsumoEntity;
import br.com.engecore.Mapper.MaterialMapper;
import br.com.engecore.Repository.InsumoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public class InsumoService {

    @Autowired
    private InsumoRepository insumoRepository;

    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public InsumoDTO cadastrar(InsumoDTO dto) {
        InsumoEntity material = new InsumoEntity();

        material.setNome(dto.getNome());
        material.setUnidade(dto.getUnidade());
        insumoRepository.save(material);
        return MaterialMapper.toDTO(material);
    }

    // Atualização feita por ADM ou FUNC para qualquer material (precisa do ID)
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public InsumoDTO atualizarPorAdmFuncionario(Long id, InsumoDTO dto) {
        InsumoEntity material = buscarMaterial(id);

        material.setNome(dto.getNome());
        material.setUnidade(dto.getUnidade());

        insumoRepository.save(material);
        return MaterialMapper.toDTO(material);
    }

    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncAdm(authentication)")
    public void deletarCliente(Long id) {
        insumoRepository.deleteById(id);
    }


    public List<InsumoEntity> listarMateriais(){
        return insumoRepository.findAll();
    }

    public InsumoDTO detalhesMaterial(Long id) {
        InsumoEntity material = insumoRepository.findById(id).orElseThrow(() -> new RuntimeException("material não encontrada!"));
        return MaterialMapper.toDTO(material);
    }

    public InsumoEntity buscarMaterial(Long id) {
        return insumoRepository.findById(id).orElseThrow(() -> new RuntimeException("material não encontrada"));
    }


}
