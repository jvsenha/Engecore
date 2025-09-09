package br.com.engecore.Service;

import br.com.engecore.DTO.FasesDTO;
import br.com.engecore.Entity.FasesEntity;
import br.com.engecore.Mapper.FasesMapper;
import br.com.engecore.Repository.FasesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public class FasesService {
    @Autowired
    private FasesRepository fasesRepository;


    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public FasesDTO cadastrar(FasesDTO dto) {
        FasesEntity fases = new FasesEntity();


        fases.setNome(dto.getNome());
        fases.setDataInicio(dto.getDataInicio());
        fases.setDataTermino(dto.getDataTermino());
        fases.setTempoEsperado(dto.getTempoEsperado());
        fases.setTempoLevado(dto.getTempoLevado());
        fases.setDescricao(dto.getDescricao());
        fasesRepository.save(fases);
        return FasesMapper.toDTO(fases);
    }

    // Atualização feita por ADM ou FUNC para qualquer fases (precisa do ID)
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public FasesDTO atualizarPorAdmFuncionario(Long id, FasesDTO dto) {
        FasesEntity fases = buscarFases(id);

        fases.setNome(dto.getNome());
        fases.setDataInicio(dto.getDataInicio());
        fases.setDataTermino(dto.getDataTermino());
        fases.setTempoEsperado(dto.getTempoEsperado());
        fases.setTempoLevado(dto.getTempoLevado());
        fases.setDescricao(dto.getDescricao());

        fases.setDescricao(dto.getDescricao());
        fasesRepository.save(fases);
        return FasesMapper.toDTO(fases);
    }

    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncAdm(authentication)")
    public void deletarFases(Long id) {
        fasesRepository.deleteById(id);
    }


    public List<FasesEntity> listarFases(){
        return fasesRepository.findAll();
    }

    public FasesDTO detalhesFases(Long id) {
        FasesEntity fases = fasesRepository.findById(id).orElseThrow(() -> new RuntimeException("Movimentação não encontrada!"));
        return FasesMapper.toDTO(fases);
    }

    public FasesEntity buscarFases(Long id) {
        return fasesRepository.findById(id).orElseThrow(() -> new RuntimeException("Movimentação não encontrada"));
    }
}
