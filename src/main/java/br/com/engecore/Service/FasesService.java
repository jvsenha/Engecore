package br.com.engecore.Service;

import br.com.engecore.DTO.FasesDTO;
import br.com.engecore.DTO.FasesResponse;
import br.com.engecore.Entity.FasesEntity;
import br.com.engecore.Entity.ObrasEntity;
import br.com.engecore.Mapper.FasesMapper;
import br.com.engecore.Repository.FasesRepository;
import br.com.engecore.Repository.ObrasRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FasesService {
    @Autowired
    private FasesRepository fasesRepository;

    @Autowired
    private ObrasRepository obrasRepository;


    @Transactional
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public FasesDTO cadastrar(FasesDTO dto) {
        FasesEntity fases = new FasesEntity();

        // Preenche os campos básicos
        fases.setNome(dto.getNome());
        fases.setDataInicio(dto.getDataInicio());
        fases.setDataTermino(dto.getDataTermino());
        fases.setTempoEsperado(dto.getTempoEsperado());
        fases.setTempoLevado(dto.getTempoLevado());
        fases.setDescricao(dto.getDescricao());

        // Define a obra obrigatoriamente (não precisa comparar com algo inexistente)
        if (dto.getObra() != null) {
            ObrasEntity obra = obrasRepository.findById(dto.getObra())
                    .orElseThrow(() -> new RuntimeException("Obra não encontrada"));
            fases.setObra(obra);
        } else {
            throw new RuntimeException("Obra é obrigatória para cadastrar uma fase");
        }

        fasesRepository.save(fases);
        return FasesMapper.toDTO(fases);
    }

    // Atualização feita por ADM ou FUNC para qualquer fases (precisa do ID)
    @Transactional
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public FasesDTO atualizarPorAdmFuncionario(Long id, FasesDTO dto) {
        FasesEntity fases = buscarFases(id);

        // Atualizar os campos
        fases.setNome(dto.getNome());
        fases.setDataInicio(dto.getDataInicio());
        fases.setDataTermino(dto.getDataTermino());
        fases.setTempoEsperado(dto.getTempoEsperado());
        fases.setTempoLevado(dto.getTempoLevado());
        fases.setDescricao(dto.getDescricao());

        // Atualizar obra se necessário
        if (dto.getObra() != null && !dto.getObra().equals(fases.getObra().getId())) {
            ObrasEntity obra = obrasRepository.findById(dto.getObra())
                    .orElseThrow(() -> new RuntimeException("Obra não encontrada"));
            fases.setObra(obra);
        }

        fasesRepository.save(fases);
        return FasesMapper.toDTO(fases);
    }

    @Transactional
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncAdm(authentication)")
    public void deletarFases(Long id) {
        fasesRepository.deleteById(id);
    }

    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncAdm(authentication)")
    public List<FasesResponse> listarFases(){
        return fasesRepository.findAll()
                .stream()
                .map(fase -> new FasesResponse(
                        fase.getId(),
                        fase.getNome(),
                        fase.getObra().getId(),
                        fase.getDataInicio(),
                        fase.getDataTermino(),
                        fase.getTempoEsperado(),
                        fase.getTempoLevado(),
                        fase.getDescricao()
                ))
                .toList();
    }

    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncAdm(authentication)")
    public List<FasesResponse> listarFasesPorObra(Long id){
        return fasesRepository.findByObraId(id)
                .stream()
                .map(fase -> new FasesResponse(
                        fase.getId(),
                        fase.getNome(),
                        fase.getObra() != null ? fase.getObra().getId() : null,
                        fase.getDataInicio(),
                        fase.getDataTermino(),
                        fase.getTempoEsperado(),
                        fase.getTempoLevado(),
                        fase.getDescricao()
                ))
                .toList();
    }

    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncAdm(authentication)")
    public FasesDTO detalhesFases(Long id) {
        FasesEntity fases = fasesRepository.findById(id).orElseThrow(() -> new RuntimeException("Movimentação não encontrada!"));
        return FasesMapper.toDTO(fases);
    }

    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncAdm(authentication)")
    public FasesEntity buscarFases(Long id) {
        return fasesRepository.findById(id).orElseThrow(() -> new RuntimeException("Movimentação não encontrada"));
    }
}
