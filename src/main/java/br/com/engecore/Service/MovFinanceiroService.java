package br.com.engecore.Service;

import br.com.engecore.DTO.MovFinanceiraDTO;
import br.com.engecore.Entity.*;
import br.com.engecore.Enum.TipoMovFinanceiro;
import br.com.engecore.Mapper.MovFinanceiraMapper;
import br.com.engecore.Repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MovFinanceiroService {

    @Autowired
    private MovFinanceiraRepository movFinanceiraRepository;
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ObrasRepository obrasRepository;

    @Autowired
    private InsumoRepository insumoRepository; // Adicionar

    @Autowired
    private FuncionarioRepository funcionarioRepository; // Adicionar

    @Transactional
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public MovFinanceiraDTO cadastrar(MovFinanceiraDTO dto) {
        MovFinanceiraEntity movFinanceira = new MovFinanceiraEntity();

        movFinanceira.setValor(dto.getValor());
        movFinanceira.setTipo(dto.getTipo());
        movFinanceira.setCategoriaFinanceira(dto.getCategoriaFinanceira());
        movFinanceira.setDataMovimento(dto.getDataMovimento());
        movFinanceira.setDescricao(dto.getDescricao());

        // Busca o funcionário responsável pelo ID
        if (dto.getFuncionarioResponsavelId() != null) {
            FuncionarioEntity funcionario = funcionarioRepository.findById(dto.getFuncionarioResponsavelId())
                    .orElseThrow(() -> new RuntimeException("Funcionário responsável não encontrado"));
            movFinanceira.setFuncionarioResponsavel(funcionario);
        }

        // Busca o insumo (se houver)
        if (dto.getInsumoId() != null) {
            InsumoEntity insumo = insumoRepository.findById(dto.getInsumoId())
                    .orElseThrow(() -> new RuntimeException("Insumo não encontrado"));
            movFinanceira.setInsumo(insumo);
        }

        // Se for uma movimentação de obra
        if (dto.getObraId() != null) {
            ObrasEntity obra = obrasRepository.findById(dto.getObraId())
                    .orElseThrow(() -> new RuntimeException("Obra não encontrada"));
            movFinanceira.setObra(obra);
            // Associa o cliente da obra à movimentação
            if (obra.getCliente() != null) {
                movFinanceira.setCliente(obra.getCliente());
            }
        } else if (dto.getClienteId() != null) { // Se for uma movimentação direta do cliente
            ClienteEntity cliente = clienteRepository.findById(dto.getClienteId())
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
            movFinanceira.setCliente(cliente);
        }
        // Se ambos obraId e clienteId forem nulos, a movimentação é da empresa

        movFinanceiraRepository.save(movFinanceira);
        // O mapper precisa ser ajustado se ele também espera entidades completas no DTO.
        // Por simplicidade, vamos retornar o DTO de entrada.
        return dto;
    }

    @Transactional
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public MovFinanceiraDTO atualizar(Long id, MovFinanceiraDTO dto) {
        // 1. Busca a movimentação existente que será atualizada
        MovFinanceiraEntity movFinanceira = movFinanceiraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movimentação financeira não encontrada com o ID: " + id));

        // 2. Atualiza os campos simples
        movFinanceira.setValor(dto.getValor());
        movFinanceira.setTipo(dto.getTipo());
        movFinanceira.setCategoriaFinanceira(dto.getCategoriaFinanceira());
        movFinanceira.setDataMovimento(dto.getDataMovimento());
        movFinanceira.setDescricao(dto.getDescricao());

        // 3. Busca e atualiza as entidades relacionadas usando os IDs do DTO

        // Atualiza o funcionário
        if (dto.getFuncionarioResponsavelId() != null) {
            FuncionarioEntity funcionario = funcionarioRepository.findById(dto.getFuncionarioResponsavelId())
                    .orElseThrow(() -> new RuntimeException("Funcionário responsável não encontrado"));
            movFinanceira.setFuncionarioResponsavel(funcionario);
        }

        // Atualiza o insumo (se houver, senão define como nulo)
        if (dto.getInsumoId() != null) {
            InsumoEntity insumo = insumoRepository.findById(dto.getInsumoId())
                    .orElseThrow(() -> new RuntimeException("Insumo não encontrado"));
            movFinanceira.setInsumo(insumo);
        } else {
            movFinanceira.setInsumo(null);
        }

        // Limpa as associações antigas antes de definir as novas
        movFinanceira.setObra(null);
        movFinanceira.setCliente(null);

        // Define as novas associações (obra ou cliente)
        if (dto.getObraId() != null) {
            ObrasEntity obra = obrasRepository.findById(dto.getObraId())
                    .orElseThrow(() -> new RuntimeException("Obra não encontrada"));
            movFinanceira.setObra(obra);
            if (obra.getCliente() != null) {
                movFinanceira.setCliente(obra.getCliente());
            }
        } else if (dto.getClienteId() != null) {
            ClienteEntity cliente = clienteRepository.findById(dto.getClienteId())
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
            movFinanceira.setCliente(cliente);
        }

        // 4. Salva a entidade atualizada
        MovFinanceiraEntity movAtualizada = movFinanceiraRepository.save(movFinanceira);

        // 5. Retorna um DTO mapeado da entidade salva
        return MovFinanceiraMapper.toDTO(movAtualizada);
    }
    @Transactional
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncAdm(authentication)")
    public void deletarMovFinanceira(Long id) {
        movFinanceiraRepository.deleteById(id);
    }

    public List<MovFinanceiraEntity> listarMovFinanceira(){
        return movFinanceiraRepository.findAll();
    }

    public List<MovFinanceiraEntity> listarPorObra(Long idObra) {
        return movFinanceiraRepository.findByObraId(idObra);
    }

    public List<MovFinanceiraEntity> listarMovEmpresa() {
        return movFinanceiraRepository.findByObraIsNullAndClienteIsNull();
    }

    public MovFinanceiraDTO detalhesMovFinanceira(Long id) {
        MovFinanceiraEntity movFinandeira = movFinanceiraRepository.findById(id).orElseThrow(() -> new RuntimeException("Movimentação não encontrada!"));
        return MovFinanceiraMapper.toDTO(movFinandeira);
    }

    public MovFinanceiraEntity buscarMovFinanceira(Long id) {
        return movFinanceiraRepository.findById(id).orElseThrow(() -> new RuntimeException("Movimentação não encontrada"));
    }

    public BigDecimal calcularSaldoEmpresa() {
        List<MovFinanceiraEntity> movimentacoes = movFinanceiraRepository.findByObraIsNullAndClienteIsNull();
        return calcularSaldo(movimentacoes);
    }

    public BigDecimal calcularSaldoObra(Long idObra) {
        List<MovFinanceiraEntity> movimentacoes = movFinanceiraRepository.findByObraId(idObra);
        return calcularSaldo(movimentacoes);
    }

    public BigDecimal calcularSaldoCliente(Long idCliente) {
        List<MovFinanceiraEntity> movimentacoes = movFinanceiraRepository.findByClienteId(idCliente);
        return calcularSaldo(movimentacoes);
    }

    // Método auxiliar privado para evitar repetição de código
    private BigDecimal calcularSaldo(List<MovFinanceiraEntity> movimentacoes) {
        BigDecimal saldo = BigDecimal.ZERO;
        for (MovFinanceiraEntity mov : movimentacoes) {
            if (mov.getTipo() == TipoMovFinanceiro.RECEITA) {
                saldo = saldo.add(mov.getValor());
            } else {
                saldo = saldo.subtract(mov.getValor());
            }
        }
        return saldo;
    }
}
