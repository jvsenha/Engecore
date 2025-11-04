package br.com.engecore.Service;

// DTOs
import br.com.engecore.DTO.AcompanhamentoFaseDTO;
import br.com.engecore.DTO.CurvaAbcDTO;
import br.com.engecore.DTO.CustoInsumoDTO;
import br.com.engecore.DTO.DreObraDTO;
import br.com.engecore.DTO.MaterialEstoqueResponse;

// Entidades
import br.com.engecore.Entity.FasesEntity;
import br.com.engecore.Entity.MaterialEstoque;
import br.com.engecore.Entity.ObrasEntity;

// Enums
import br.com.engecore.Enum.CategoriaFinanceira;
import br.com.engecore.Enum.TipoMovFinanceiro;

// Mappers
import br.com.engecore.Mapper.MaterialEstoqueMapper;

// Repositories
import br.com.engecore.Repository.FasesRepository;
import br.com.engecore.Repository.MaterialEstoqueRepository;
import br.com.engecore.Repository.MovFinanceiraRepository;
import br.com.engecore.Repository.ObrasRepository;

// Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Java Util
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RelatorioService {

    @Autowired
    private ObrasRepository obrasRepository;

    @Autowired
    private MovFinanceiraRepository movFinanceiraRepository;

    @Autowired
    private MaterialEstoqueRepository materialEstoqueRepository;

    @Autowired
    private FasesRepository fasesRepository;

    /**
     * RELATÓRIO 1: DRE Simplificado da Obra (Financeiro vs. Orçado)
     * Responde: "Estou tendo lucro ou prejuízo nesta obra?"
     */
    public DreObraDTO getDrePorObra(Long obraId) {
        // 1. Buscar a obra
        ObrasEntity obra = obrasRepository.findById(obraId)
                .orElseThrow(() -> new RuntimeException("Obra com ID " + obraId + " não encontrada."));

        // 2. Buscar o custo total (soma das despesas)
        BigDecimal custoTotal = movFinanceiraRepository.sumDespesasByObraId(obraId, TipoMovFinanceiro.DESPESA);

        // Se não houver despesas, o SUM retorna null. Precisamos tratar isso.
        if (custoTotal == null) {
            custoTotal = BigDecimal.ZERO;
        }

        // 3. Buscar o valor contratado (orçamento)
        BigDecimal valorContratado = obra.getValorTotal();
        if (valorContratado == null) {
            valorContratado = BigDecimal.ZERO;
        }

        // 4. Calcular o saldo
        BigDecimal saldo = valorContratado.subtract(custoTotal);

        // 5. Montar o DTO de resposta
        return new DreObraDTO(
                obra.getId(),
                obra.getNomeObra(),
                valorContratado,
                custoTotal,
                saldo,
                obra.getStatusConst() != null ? obra.getStatusConst().name() : "N/A"
        );
    }

    /**
     * RELATÓRIO 2: Estoque Crítico (Itens a comprar)
     * Responde: "O que preciso comprar urgentemente?"
     */
    public List<MaterialEstoqueResponse> getEstoqueCritico() {
        // 1. Busca as entidades do banco usando a query customizada
        List<MaterialEstoque> criticos = materialEstoqueRepository.findEstoqueCritico();

        // 2. Converte a lista de Entidades para uma lista de DTOs de Resposta
        // (Note que o Mapper.toResponse é estático, então não precisamos injetá-lo)
        return criticos.stream()
                .map(MaterialEstoqueMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * RELATÓRIO 3: Curva ABC de Insumos (Maiores custos)
     * Responde: "Quais insumos mais impactam meu orçamento?"
     */
    public List<CurvaAbcDTO> getCurvaABCInsumos() {
        // 1. Buscar a lista de custos por insumo, já ordenada
        List<CustoInsumoDTO> custos = movFinanceiraRepository.findCustoPorInsumo(
                TipoMovFinanceiro.DESPESA,
                CategoriaFinanceira.COMPRA_MATERIAL
        );

        if (custos.isEmpty()) {
            return new ArrayList<>(); // Retorna lista vazia se não houver despesas
        }

        // 2. Calcular o Custo Total Geral (soma de todos os insumos)
        BigDecimal grandTotal = custos.stream()
                .map(CustoInsumoDTO::getCustoTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 3. Calcular percentuais e percentuais acumulados
        List<CurvaAbcDTO> relatorioFinal = new ArrayList<>();
        double percentualAcumulado = 0.0;

        for (CustoInsumoDTO custo : custos) {
            // Calcula o percentual do item (custoItem / custoTotal)
            double percentual = 0.0;
            if (grandTotal.compareTo(BigDecimal.ZERO) > 0) {
                percentual = custo.getCustoTotal()
                        .divide(grandTotal, 4, RoundingMode.HALF_UP) // Divide com 4 casas de precisão
                        .doubleValue() * 100;
            }

            percentualAcumulado += percentual;

            relatorioFinal.add(new CurvaAbcDTO(
                    custo.getInsumoId(),
                    custo.getInsumoNome(),
                    custo.getCustoTotal(),
                    // Arredonda para 2 casas decimais
                    Math.round(percentual * 100.0) / 100.0,
                    Math.round(percentualAcumulado * 100.0) / 100.0
            ));
        }

        return relatorioFinal;
    }

    /**
     * RELATÓRIO 4: Acompanhamento de Fases (Cronograma Físico)
     * Responde: "A obra está no prazo?"
     */
    public List<AcompanhamentoFaseDTO> getAcompanhamentoFases(Long obraId) {
        // 1. Busca todas as fases da obra
        List<FasesEntity> fases = fasesRepository.findByObraId(obraId);
        if (fases.isEmpty()) {
            // Retorna lista vazia se a obra não tiver fases
            return new ArrayList<>();
        }

        LocalDate hoje = LocalDate.now();

        // 2. Mapeia cada entidade para o DTO, calculando o status
        return fases.stream().map(fase -> {
            // Calcula a data prevista de término
            LocalDate dataPrevistaTermino = null;
            if (fase.getDataInicio() != null && fase.getTempoEsperado() != null && fase.getTempoEsperado() > 0) {
                dataPrevistaTermino = fase.getDataInicio().plusDays(fase.getTempoEsperado());
            }

            // Calcula o status do prazo
            String statusPrazo = "Indefinido";
            if (fase.getDataTermino() != null) { // Fase Concluída
                if (dataPrevistaTermino != null && fase.getDataTermino().isAfter(dataPrevistaTermino)) {
                    statusPrazo = "Concluída com Atraso";
                } else {
                    statusPrazo = "Concluída";
                }
            } else if (dataPrevistaTermino != null) { // Fase Em Andamento
                if (hoje.isAfter(dataPrevistaTermino)) {
                    statusPrazo = "Atrasada";
                } else {
                    statusPrazo = "Em Dia";
                }
            } else if (fase.getDataInicio() != null) { // Iniciada mas sem previsão
                statusPrazo = "Em Andamento";
            } else {
                statusPrazo = "Planejada";
            }

            // 3. Monta o DTO
            return new AcompanhamentoFaseDTO(
                    fase.getId(),
                    fase.getNome(),
                    statusPrazo,
                    fase.getDataInicio(),
                    dataPrevistaTermino,
                    fase.getDataTermino(), // dataTerminoReal
                    fase.getTempoEsperado(),
                    fase.getTempoLevado(),
                    fase.getDescricao()
            );
        }).collect(Collectors.toList());
    }
}