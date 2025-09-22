package br.com.engecore.Service;

import br.com.engecore.DTO.InsumoDTO;
import br.com.engecore.Entity.InsumoEntity;
import br.com.engecore.Mapper.InsumoMapper;
import br.com.engecore.Repository.InsumoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InsumoService {

    @Autowired
    private InsumoRepository insumoRepository;

    @Transactional
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public InsumoDTO cadastrar(InsumoDTO dto) {
        InsumoEntity insumo = new InsumoEntity();

        insumo.setNome(dto.getNome());
        insumo.setUnidade(dto.getUnidade());
        insumoRepository.save(insumo);
        return InsumoMapper.toDTO(insumo);
    }

    @Transactional
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public InsumoDTO atualizarPorAdmFuncionario(Long id, InsumoDTO dto) {
        InsumoEntity insumo = buscarInsumo(id);

        insumo.setNome(dto.getNome());
        insumo.setUnidade(dto.getUnidade());

        insumoRepository.save(insumo);
        return InsumoMapper.toDTO(insumo);
    }

    @Transactional
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncAdm(authentication)")
    public void deletarInsumo(Long id) {
        insumoRepository.deleteById(id);
    }


    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncAdm(authentication)")
    public List<InsumoEntity> listar(){
        return insumoRepository.findAll();
    }

    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncAdm(authentication)")
    public InsumoDTO detalhesInsumo(Long id) {
        InsumoEntity insumo = insumoRepository.findById(id).orElseThrow(() -> new RuntimeException("insumo não encontrada!"));
        return InsumoMapper.toDTO(insumo);
    }

    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncAdm(authentication)")
    public InsumoEntity buscarInsumo(Long id) {
        return insumoRepository.findById(id).orElseThrow(() -> new RuntimeException("insumo não encontrada"));
    }


}
