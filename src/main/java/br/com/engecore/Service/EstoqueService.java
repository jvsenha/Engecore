package br.com.engecore.Service;

import br.com.engecore.DTO.EstoqueDTO;
import br.com.engecore.Entity.EstoqueEntity;
import br.com.engecore.Mapper.EstoqueMapper;
import br.com.engecore.Repository.EstoqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstoqueService {

    @Autowired
    private EstoqueRepository estoqueRepository;



    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public EstoqueDTO cadastrar(EstoqueDTO dto) {
        EstoqueEntity fases = new EstoqueEntity();
        fases.setNome(dto.getNome());
        fases.setTipo(dto.getTipo());
        fases.setEstoqueMateriais(dto.getEstoqueMateriais());
        estoqueRepository.save(fases);
        return EstoqueMapper.toDTO(fases);
    }

    // Atualização feita por ADM ou FUNC para qualquer fases (precisa do ID)
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public EstoqueDTO atualizarPorAdmFuncionario(Long id, EstoqueDTO dto) {
        EstoqueEntity fases = buscarEstoque(id);

        fases.setNome(dto.getNome());
        fases.setTipo(dto.getTipo());
        fases.setEstoqueMateriais(dto.getEstoqueMateriais());
        estoqueRepository.save(fases);
        return EstoqueMapper.toDTO(fases);
    }

    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncAdm(authentication)")
    public void deletarEstoque(Long id) {
        estoqueRepository.deleteById(id);
    }


    public List<EstoqueEntity> listar(){
        return estoqueRepository.findAll();
    }

    public EstoqueDTO detalhesEstoque(Long id) {
        EstoqueEntity fases = estoqueRepository.findById(id).orElseThrow(() -> new RuntimeException("Estoque não encontrada!"));
        return EstoqueMapper.toDTO(fases);
    }

    public EstoqueEntity buscarEstoque(Long id) {
        return estoqueRepository.findById(id).orElseThrow(() -> new RuntimeException("Estoque não encontrada"));
    }
}
