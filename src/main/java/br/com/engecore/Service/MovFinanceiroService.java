package br.com.engecore.Service;

import br.com.engecore.DTO.MovFinanceiraDTO;
import br.com.engecore.Entity.MovFinanceiraEntity;
import br.com.engecore.Mapper.MovFinanceiraMapper;
import br.com.engecore.Repository.MovFinanceiraRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovFinanceiroService {

    @Autowired
    private MovFinanceiraRepository movFinanceiraRepository;

    @Transactional
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public MovFinanceiraDTO cadastrar(MovFinanceiraDTO dto) {
        MovFinanceiraEntity movFinandeira = new MovFinanceiraEntity();

        movFinandeira.setTipo(dto.getTipo());
        movFinandeira.setInsumo(dto.getInsumo());
        movFinandeira.setObra(dto.getObra());
        movFinandeira.setFuncionarioResponsavel(dto.getFuncionarioResponsavel());
        movFinandeira.setCategoriaFinanceira(dto.getCategoriaFinanceira());
        movFinandeira.setDataMovimento(dto.getDataMovimento());
        movFinandeira.setValor(dto.getValor());
        movFinandeira.setDescricao(dto.getDescricao());
        movFinanceiraRepository.save(movFinandeira);
        return MovFinanceiraMapper.toDTO(movFinandeira);
    }

    @Transactional
    // Atualização feita por ADM ou FUNC para qualquer movFinandeira (precisa do ID)
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public MovFinanceiraDTO atualizarPorAdmFuncionario(Long id, MovFinanceiraDTO dto) {
        MovFinanceiraEntity movFinandeira = buscarMovFinanceira(id);

        movFinandeira.setTipo(dto.getTipo());
        movFinandeira.setInsumo(dto.getInsumo());
        movFinandeira.setObra(dto.getObra());
        movFinandeira.setFuncionarioResponsavel(dto.getFuncionarioResponsavel());
        movFinandeira.setCategoriaFinanceira(dto.getCategoriaFinanceira());
        movFinandeira.setDataMovimento(dto.getDataMovimento());
        movFinandeira.setValor(dto.getValor());
        movFinandeira.setDescricao(dto.getDescricao());
        movFinanceiraRepository.save(movFinandeira);
        return MovFinanceiraMapper.toDTO(movFinandeira);
    }

    @Transactional
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncAdm(authentication)")
    public void deletarMovFinanceira(Long id) {
        movFinanceiraRepository.deleteById(id);
    }


    public List<MovFinanceiraEntity> listarMovFinanceira(){
        return movFinanceiraRepository.findAll();
    }

    public MovFinanceiraDTO detalhesMovFinanceira(Long id) {
        MovFinanceiraEntity movFinandeira = movFinanceiraRepository.findById(id).orElseThrow(() -> new RuntimeException("Movimentação não encontrada!"));
        return MovFinanceiraMapper.toDTO(movFinandeira);
    }

    public MovFinanceiraEntity buscarMovFinanceira(Long id) {
        return movFinanceiraRepository.findById(id).orElseThrow(() -> new RuntimeException("Movimentação não encontrada"));
    }
}
