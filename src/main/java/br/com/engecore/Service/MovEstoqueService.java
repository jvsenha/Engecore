package br.com.engecore.Service;

import br.com.engecore.DTO.MaterialEstoqueRequest;
import br.com.engecore.DTO.MovEstoqueDTO;
import br.com.engecore.DTO.MovEstoqueResponse;
import br.com.engecore.Entity.*;
import br.com.engecore.Enum.CategoriaFinanceira;
import br.com.engecore.Enum.TipoMovFinanceiro;
import br.com.engecore.Mapper.MovEstoqueMapper;
import br.com.engecore.Repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class MovEstoqueService {

    @Autowired
    private MovEstoqueRepository movEstoqueRepository;

    @Autowired
    private MaterialEstoqueRepository materialEstoqueRepository;

    @Autowired
    private MovFinanceiraRepository movFinanceiraRepository;

    @Autowired
    private InsumoRepository insumoRepository;

    @Autowired
    private EstoqueRepository estoqueRepository;

    @Autowired
    private FuncionarioRepository  funcionarioRepository;

    @Autowired
    private ObrasRepository  obrasRepository;


    @Transactional
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public MovEstoqueDTO atualizarPorAdmFuncionario(Long id, MovEstoqueDTO dto) {
        MovEstoqueEntity movAntigo = buscarMovEstoque(id);

        // Reverter movimentação antiga
        reverterMovimentacao(movAntigo);

        // Atualizar dados
        movAntigo.setTipoMov(dto.getTipoMov());
        movAntigo.setInsumo(buscarInsumo(dto.getInsumoId()));
        movAntigo.setQuantidade(dto.getQuantidade());
        movAntigo.setFuncionarioResponsavel(buscarFuncionario(dto.getFuncionarioId()));
        movAntigo.setDataMovimentacao(dto.getDataMovimentacao());
        movAntigo.setEstoqueOrigem(buscarEstoque(dto.getEstoqueOrigemId()));
        movAntigo.setEstoqueDestino(buscarEstoque(dto.getEstoqueDestinoId()));

        movEstoqueRepository.save(movAntigo);

        // Aplicar movimentação nova
        aplicarMovimentacao(movAntigo, dto.getMaterialEstoque());

        return MovEstoqueMapper.toDTO(movAntigo);
    }

    @Transactional
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncAdm(authentication)")
    public void deletarMovEstoque(Long id) {
        MovEstoqueEntity movAntigo = buscarMovEstoque(id);
        reverterMovimentacao(movAntigo);
        movEstoqueRepository.deleteById(id);
    }

    @Transactional
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public MovEstoqueDTO cadastrar(MovEstoqueDTO dto) {
        // Cria MovEstoqueEntity
        MovEstoqueEntity mov = new MovEstoqueEntity();
        mov.setTipoMov(dto.getTipoMov());
        mov.setInsumo(buscarInsumo(dto.getInsumoId()));
        mov.setQuantidade(dto.getQuantidade());
        mov.setFuncionarioResponsavel(buscarFuncionario(dto.getFuncionarioId()));
        mov.setDataMovimentacao(dto.getDataMovimentacao() != null ? dto.getDataMovimentacao() : LocalDate.now());
        mov.setEstoqueOrigem(buscarEstoque(dto.getEstoqueOrigemId()));
        mov.setEstoqueDestino(buscarEstoque(dto.getEstoqueDestinoId()));

        movEstoqueRepository.save(mov);

        // Aplica movimentação
        aplicarMovimentacao(mov, dto.getMaterialEstoque());

        return MovEstoqueMapper.toDTO(mov);
    }

    @Transactional
    private void aplicarMovimentacao(MovEstoqueEntity mov, MaterialEstoqueRequest request) {
        switch (mov.getTipoMov()) {
            case ENTRADA:
                registrarEntrada(mov, request);
                registrarDespesaFinanceira(mov);
                break;
            case SAIDA:
                registrarSaida(mov);
                break;
            case TRANSFERENCIA:
                registrarTransferencia(mov, request);
                break;
            case AJUSTE:
                registrarAjuste(mov, request);
                break;
        }
    }

    @Transactional
    private void registrarEntrada(MovEstoqueEntity mov, MaterialEstoqueRequest request) {
        if (mov.getEstoqueDestino() == null) {
            throw new RuntimeException("Estoque de destino não informado para entrada.");
        }

        // Busca ou cria MaterialEstoque
        MaterialEstoque mat = materialEstoqueRepository
                .findByEstoqueAndMaterial(mov.getEstoqueDestino(), mov.getInsumo())
                .orElseGet(() -> criarNovoMaterialEstoque(mov.getEstoqueDestino(), mov.getInsumo(), request));

        // Atualiza a quantidade
        mat.setQuantidadeAtual(mat.getQuantidadeAtual().add(mov.getQuantidade()));
        materialEstoqueRepository.save(mat);
    }

    @Transactional
    private void registrarSaida(MovEstoqueEntity mov) {
        if (mov.getEstoqueOrigem() == null) {
            throw new RuntimeException("Estoque de origem não informado para saída.");
        }

        MaterialEstoque mat = materialEstoqueRepository
                .findByEstoqueAndMaterial(mov.getEstoqueOrigem(), mov.getInsumo())
                .orElseThrow(() -> new RuntimeException("Material não encontrado no estoque de origem."));

        if (mat.getQuantidadeAtual().compareTo(mov.getQuantidade()) < 0) {
            throw new RuntimeException("Quantidade insuficiente no estoque.");
        }

        mat.setQuantidadeAtual(mat.getQuantidadeAtual().subtract(mov.getQuantidade()));
        materialEstoqueRepository.save(mat);
    }

    @Transactional
    private void registrarTransferencia(MovEstoqueEntity mov, MaterialEstoqueRequest request) {
        if (mov.getEstoqueOrigem() == null || mov.getEstoqueDestino() == null) {
            throw new RuntimeException("Estoques de origem e destino devem ser informados para transferência.");
        }

        // Retira do estoque de origem
        registrarSaida(mov);

        // Adiciona no estoque de destino
        MaterialEstoque matDestino = materialEstoqueRepository
                .findByEstoqueAndMaterial(mov.getEstoqueDestino(), mov.getInsumo())
                .orElseGet(() -> criarNovoMaterialEstoque(mov.getEstoqueDestino(), mov.getInsumo(), request));

        matDestino.setQuantidadeAtual(matDestino.getQuantidadeAtual().add(mov.getQuantidade()));
        materialEstoqueRepository.save(matDestino);
    }

    @Transactional
    private void registrarAjuste(MovEstoqueEntity mov, MaterialEstoqueRequest request) {
        if (mov.getEstoqueDestino() == null && mov.getEstoqueOrigem() == null) {
            throw new RuntimeException("É necessário informar pelo menos um estoque para ajuste.");
        }

        MaterialEstoque mat = materialEstoqueRepository
                .findByEstoqueAndMaterial(
                        mov.getEstoqueDestino() != null ? mov.getEstoqueDestino() : mov.getEstoqueOrigem(),
                        mov.getInsumo()
                )
                .orElseGet(() -> criarNovoMaterialEstoque(
                        mov.getEstoqueDestino() != null ? mov.getEstoqueDestino() : mov.getEstoqueOrigem(),
                        mov.getInsumo(), request
                ));

        mat.setQuantidadeAtual(mat.getQuantidadeAtual().add(mov.getQuantidade()));
        materialEstoqueRepository.save(mat);
    }

    @Transactional
    private void registrarDespesaFinanceira(MovEstoqueEntity mov) {


        // Verifica se a obra existe no banco
        ObrasEntity obra = obrasRepository.findById(mov.getEstoqueDestino().getObra().getId())
                .orElseThrow(() -> new RuntimeException("Obra não encontrada. Operação abortada."));

        // Verifica se o material existe no estoque de destino
        MaterialEstoque mat = materialEstoqueRepository
                .findByEstoqueAndMaterial(mov.getEstoqueDestino(), mov.getInsumo())
                .orElseThrow(() -> new RuntimeException("Material não encontrado para gerar despesa financeira."));

        // Cria a movimentação financeira
        MovFinanceiraEntity movFin = new MovFinanceiraEntity();
        movFin.setValor(calcularValorCompra(mov, mat));
        movFin.setTipo(TipoMovFinanceiro.DESPESA);
        movFin.setCategoriaFinanceira(CategoriaFinanceira.COMPRA_MATERIAL);
        movFin.setObra(obra);
        movFin.setInsumo(mov.getInsumo());
        movFin.setFuncionarioResponsavel(mov.getFuncionarioResponsavel());
        movFin.setDataMovimento(LocalDate.now());
        movFin.setDescricao("Compra de insumo para obra");

        movFinanceiraRepository.save(movFin);
    }


    private MaterialEstoque criarNovoMaterialEstoque(EstoqueEntity estoque, InsumoEntity material, MaterialEstoqueRequest request) {
        MaterialEstoque novo = new MaterialEstoque();
        novo.setEstoque(estoque);
        novo.setMaterial(material);
        novo.setQuantidadeAtual(BigDecimal.ZERO);
        novo.setValor(request != null && request.getValor() != null ? request.getValor() : BigDecimal.ZERO);
        novo.setQuantidadeMinima(request != null && request.getQuantidadeMinima() != null ? request.getQuantidadeMinima() : BigDecimal.ZERO);
        novo.setQuantidadeMaxima(request != null && request.getQuantidadeMaxima() != null ? request.getQuantidadeMaxima() : BigDecimal.ZERO);
        return materialEstoqueRepository.save(novo);
    }

    private BigDecimal calcularValorCompra(MovEstoqueEntity mov, MaterialEstoque mat) {
        return mat.getValor().multiply(mov.getQuantidade());
    }

    // Métodos auxiliares
    private InsumoEntity buscarInsumo(Long id) {
        if (id == null) throw new RuntimeException("ID de insumo não informado");
        return insumoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Insumo não encontrado: " + id));
    }

    private EstoqueEntity buscarEstoque(Long id) {
        if (id == null) return null;
        return estoqueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado: " + id));
    }

    private FuncionarioEntity buscarFuncionario(Long id) {
        if (id == null) throw new RuntimeException("ID do funcionário não informado");
        return funcionarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado: " + id));
    }

    public List<MovEstoqueResponse> listarMovEstoque() {
        return movEstoqueRepository.findAll()
                .stream()
                .map(MovEstoqueMapper::toResponse) // converte cada entidade em um DTO de resposta
                .toList(); // retorna como lista
    }

    public MovEstoqueDTO detalhesMovEstoque(Long id) {
        MovEstoqueEntity mov = movEstoqueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movimentação não encontrada!"));
        return MovEstoqueMapper.toDTO(mov);
    }

    public MovEstoqueEntity buscarMovEstoque(Long id) {
        return movEstoqueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movimentação não encontrada"));
    }

    @Transactional
    private void reverterMovimentacao(MovEstoqueEntity mov) {
        switch (mov.getTipoMov()) {
            case ENTRADA -> {
                MaterialEstoque mat = materialEstoqueRepository.findByEstoqueAndMaterial(mov.getEstoqueDestino(), mov.getInsumo()).orElse(null);
                if (mat != null) {
                    mat.setQuantidadeAtual(mat.getQuantidadeAtual().subtract(mov.getQuantidade()));
                    materialEstoqueRepository.save(mat);
                }
            }
            case SAIDA -> {
                MaterialEstoque mat = materialEstoqueRepository.findByEstoqueAndMaterial(mov.getEstoqueOrigem(), mov.getInsumo()).orElseThrow(() -> new RuntimeException("Material não encontrado no estoque de origem."));
                mat.setQuantidadeAtual(mat.getQuantidadeAtual().add(mov.getQuantidade()));
                materialEstoqueRepository.save(mat);
            }
            case TRANSFERENCIA -> {
                MaterialEstoque origem = materialEstoqueRepository.findByEstoqueAndMaterial(mov.getEstoqueOrigem(), mov.getInsumo()).orElseThrow(() -> new RuntimeException("Material não encontrado no estoque de origem."));
                origem.setQuantidadeAtual(origem.getQuantidadeAtual().add(mov.getQuantidade()));
                materialEstoqueRepository.save(origem);
                MaterialEstoque destino = materialEstoqueRepository.findByEstoqueAndMaterial(mov.getEstoqueDestino(), mov.getInsumo()).orElse(null);
                if (destino != null) {
                    destino.setQuantidadeAtual(destino.getQuantidadeAtual().subtract(mov.getQuantidade()));
                    materialEstoqueRepository.save(destino);
                }
            }
            case AJUSTE -> {
                MaterialEstoque mat = materialEstoqueRepository.findByEstoqueAndMaterial(mov.getEstoqueDestino() != null ? mov.getEstoqueDestino() : mov.getEstoqueOrigem(), mov.getInsumo()).orElseThrow(() -> new RuntimeException("Material não encontrado para ajuste."));
                mat.setQuantidadeAtual(mat.getQuantidadeAtual().subtract(mov.getQuantidade()));
                materialEstoqueRepository.save(mat);
            }
        }
    }

}
