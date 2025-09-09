package br.com.engecore.Service;

import br.com.engecore.DTO.MovEstoqueDTO;
import br.com.engecore.Entity.MovEstoqueEntity;
import br.com.engecore.Mapper.MovEstoqueMapper;
import br.com.engecore.Repository.MovEstoqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public class MovEstoqueService {

    @Autowired
    private MovEstoqueRepository movEstoqueRepository;

    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public MovEstoqueDTO cadastrar(MovEstoqueDTO dto) {
        MovEstoqueEntity movEstoque = new MovEstoqueEntity();

        movEstoque.setTipoMov(dto.getTipoMov());
        movEstoque.setInsumo(dto.getInsumo());
        movEstoque.setQuantidade(dto.getQuantidade());
        movEstoque.setFuncionarioResponsavel(dto.getFuncionarioResponsavel());
        movEstoque.setDataMovimentacao(dto.getDataMovimentacao());
        movEstoque.setEstoqueOrigem(dto.getEstoqueOrigem());
        movEstoque.setEstoqueDestino(dto.getEstoqueDestino());
        movEstoqueRepository.save(movEstoque);
        return MovEstoqueMapper.toDTO(movEstoque);
    }

    // Atualização feita por ADM ou FUNC para qualquer movEstoque (precisa do ID)
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public MovEstoqueDTO atualizarPorAdmFuncionario(Long id, MovEstoqueDTO dto) {
        MovEstoqueEntity movEstoque = buscarMovEstoque(id);

        movEstoque.setTipoMov(dto.getTipoMov());
        movEstoque.setInsumo(dto.getInsumo());
        movEstoque.setQuantidade(dto.getQuantidade());
        movEstoque.setFuncionarioResponsavel(dto.getFuncionarioResponsavel());
        movEstoque.setDataMovimentacao(dto.getDataMovimentacao());
        movEstoque.setEstoqueOrigem(dto.getEstoqueOrigem());
        movEstoque.setEstoqueDestino(dto.getEstoqueDestino());
        movEstoqueRepository.save(movEstoque);
        return MovEstoqueMapper.toDTO(movEstoque);
    }

    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncAdm(authentication)")
    public void deletarMovEstoque(Long id) {
        movEstoqueRepository.deleteById(id);
    }


    public List<MovEstoqueEntity> listarMovEstoque(){
        return movEstoqueRepository.findAll();
    }

    public MovEstoqueDTO detalhesMovEstoque(Long id) {
        MovEstoqueEntity movEstoque = movEstoqueRepository.findById(id).orElseThrow(() -> new RuntimeException("Movimentação não encontrada!"));
        return MovEstoqueMapper.toDTO(movEstoque);
    }

    public MovEstoqueEntity buscarMovEstoque(Long id) {
        return movEstoqueRepository.findById(id).orElseThrow(() -> new RuntimeException("Movimentação não encontrada"));
    }
}
