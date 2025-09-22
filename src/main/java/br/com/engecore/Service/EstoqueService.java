package br.com.engecore.Service;

import br.com.engecore.DTO.EstoqueDTO;
import br.com.engecore.DTO.EstoqueResponse;
import br.com.engecore.DTO.MaterialEstoqueRequest;
import br.com.engecore.Entity.EstoqueEntity;
import br.com.engecore.Entity.InsumoEntity;
import br.com.engecore.Entity.MaterialEstoque;
import br.com.engecore.Entity.ObrasEntity;
import br.com.engecore.Mapper.EstoqueMapper;
import br.com.engecore.Repository.EstoqueRepository;
import br.com.engecore.Repository.InsumoRepository;
import br.com.engecore.Repository.MaterialEstoqueRepository;
import br.com.engecore.Repository.ObrasRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EstoqueService {

    @Autowired
    private EstoqueRepository estoqueRepository;


    @Autowired
    private ObrasRepository obrasRepository;

    @Autowired
    private MaterialEstoqueRepository materialEstoqueRepository;

    @Autowired
    private InsumoRepository insumoRepository;



    @Transactional
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public EstoqueResponse cadastrar(EstoqueDTO dto) {

        // 1. Cria entidade Estoque
        EstoqueEntity estoque = new EstoqueEntity();
        estoque.setNome(dto.getNome());
        estoque.setTipo(dto.getTipo());

        // 2. Busca a obra
        ObrasEntity obra = obrasRepository.findById(dto.getObra())
                .orElseThrow(() -> new RuntimeException("Obra não encontrada"));
        estoque.setObra(obra);

        // 3. Salva estoque vazio primeiro para gerar ID
        estoqueRepository.save(estoque);

        // 4. Associa materiais
        List<MaterialEstoque> materiais = new ArrayList<>();
        for (MaterialEstoqueRequest req : dto.getEstoqueMateriais()) {
            MaterialEstoque matEstoque = new MaterialEstoque();

            InsumoEntity insumo = insumoRepository.findById(req.getMaterialId())
                    .orElseThrow(() -> new RuntimeException("Insumo não encontrado: " + req.getMaterialId()));

            matEstoque.setEstoque(estoque);
            matEstoque.setMaterial(insumo);
            matEstoque.setQuantidadeAtual(req.getQuantidadeAtual());
            matEstoque.setQuantidadeMinima(req.getQuantidadeMinima());
            matEstoque.setQuantidadeMaxima(req.getQuantidadeMaxima());

            materiais.add(matEstoque);
        }

        estoque.setEstoqueMateriais(materiais);

        // 5. Salva tudo junto
        estoqueRepository.save(estoque);

        return EstoqueMapper.toDTO(estoque);
    }
    @Transactional
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public EstoqueResponse atualizarPorAdmFuncionario(Long id, EstoqueDTO dto) {
        EstoqueEntity estoque = buscarEstoque(id);

        estoque.setNome(dto.getNome());
        estoque.setTipo(dto.getTipo());

        if (dto.getObra() != null) {
            estoque.setObra(obrasRepository.findById(dto.getObra())
                    .orElseThrow(() -> new RuntimeException("Obra não encontrada!")));
        }

        if (dto.getEstoqueMateriais() != null) {
            estoque.getEstoqueMateriais().clear();

            List<MaterialEstoque> materiais = new ArrayList<>();
            for (MaterialEstoqueRequest req : dto.getEstoqueMateriais()) {
                MaterialEstoque matEstoque = new MaterialEstoque();

                InsumoEntity insumo = insumoRepository.findById(req.getMaterialId())
                        .orElseThrow(() -> new RuntimeException("Insumo não encontrado: " + req.getMaterialId()));

                matEstoque.setEstoque(estoque);
                matEstoque.setMaterial(insumo);
                matEstoque.setQuantidadeAtual(req.getQuantidadeAtual());
                matEstoque.setQuantidadeMinima(req.getQuantidadeMinima());
                matEstoque.setQuantidadeMaxima(req.getQuantidadeMaxima());

                materiais.add(matEstoque);
            }

            estoque.getEstoqueMateriais().addAll(materiais);
        }

        estoqueRepository.save(estoque);
        return EstoqueMapper.toDTO(estoque);
    }

    @Transactional
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncAdm(authentication)")
    public void deletarEstoque(Long id) {
        estoqueRepository.deleteById(id);
    }


    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncAdm(authentication)")
    public List<EstoqueEntity> listar(){
        return estoqueRepository.findAll();
    }

    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncAdm(authentication)")
    public EstoqueResponse detalhesEstoque(Long id) {
        EstoqueEntity fases = estoqueRepository.findById(id).orElseThrow(() -> new RuntimeException("Estoque não encontrada!"));
        return EstoqueMapper.toDTO(fases);
    }

    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncAdm(authentication)")
    public EstoqueEntity buscarEstoque(Long id) {
        return estoqueRepository.findById(id).orElseThrow(() -> new RuntimeException("Estoque não encontrada"));
    }
}
