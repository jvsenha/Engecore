package br.com.engecore.Service;

import br.com.engecore.DTO.*;
import br.com.engecore.Entity.*;
import br.com.engecore.Enum.CategoriaFinanceira;
import br.com.engecore.Enum.TipoMov;
import br.com.engecore.Enum.TipoMovFinanceiro;
import br.com.engecore.Enum.TipoPessoa;
import br.com.engecore.Mapper.CotacaoMapper; // Importa o novo Mapper
import br.com.engecore.Repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CotacaoService {

    // Repositórios de Cotação
    @Autowired private CotacaoRepository cotacaoRepository;
    @Autowired private PropostaCotacaoRepository propostaCotacaoRepository;

    // Repositórios de Entidades Relacionadas
    @Autowired private ProdutoFornecedorRepository produtoFornecedorRepository;
    @Autowired private InsumoRepository insumoRepository;
    @Autowired private ObrasRepository obrasRepository;
    @Autowired private FuncionarioRepository funcionarioRepository;
    @Autowired private UsuarioJuridicoRepository usuarioJuridicoRepository;
    @Autowired private UsuarioFisicoRepository usuarioFisicoRepository;

    // Outros Serviços
    @Autowired private MovEstoqueService movEstoqueService;
    @Autowired private MovFinanceiroService movFinanceiroService;

    // Mapper
    @Autowired private CotacaoMapper cotacaoMapper; // Injeta o Mapper

    /**
     * Passo 1: O usuário solicita uma cotação (Tela CotacaoDeValores.jsx)
     * Isso cria a Cotação "Pai" e busca automaticamente as propostas.
     */
    @Transactional
    public CotacaoEntity solicitarCotacao(CotacaoRequestDTO dto) {
        // 1. Encontrar as entidades principais
        ObrasEntity obra = obrasRepository.findById(dto.getObraId())
                .orElseThrow(() -> new RuntimeException("Obra não encontrada: " + dto.getObraId()));
        InsumoEntity insumo = insumoRepository.findById(dto.getInsumoId())
                .orElseThrow(() -> new RuntimeException("Insumo não encontrado: " + dto.getInsumoId()));
        FuncionarioEntity func = funcionarioRepository.findById(dto.getFuncionarioId())
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado: " + dto.getFuncionarioId()));

        // 2. Criar e salvar a Cotação principal
        CotacaoEntity cotacao = new CotacaoEntity();
        cotacao.setObra(obra);
        cotacao.setInsumo(insumo);
        cotacao.setQuantidade(dto.getQuantidade());
        cotacao.setDataNecessidade(dto.getDataNecessidade());
        cotacao.setPrioridade(dto.getPrioridade());
        cotacao.setFuncionarioSolicitante(func);
        cotacao.setStatus("ABERTA");
        cotacao = cotacaoRepository.save(cotacao);

        // 3. (MODO AUTOMÁTICO) Encontrar todos os fornecedores que vendem este insumo
        List<ProdutoFornecedorEntity> produtos = produtoFornecedorRepository.findByInsumoId(insumo.getId());
        if (produtos.isEmpty()) {
            throw new RuntimeException("Nenhum fornecedor cadastrado para o insumo: " + insumo.getNome());
        }

        // 4. Criar uma Proposta para cada fornecedor encontrado
        List<PropostaCotacaoEntity> propostas = new ArrayList<>();

        // Lógica simples para encontrar o melhor preço
        ProdutoFornecedorEntity melhorProduto = produtos.stream()
                .min((p1, p2) -> p1.getValor().compareTo(p2.getValor()))
                .orElse(null);

        for (ProdutoFornecedorEntity produto : produtos) {
            PropostaCotacaoEntity proposta = new PropostaCotacaoEntity();
            proposta.setCotacao(cotacao);
            proposta.setProdutoFornecedor(produto);
            proposta.setValorUnitario(produto.getValor()); // Pega o preço de tabela

            // Define os campos do frontend
            proposta.setPrazoEntrega(produto.getPrazoEntrega()); // Assumindo que você adicionará isso ao ProdutoFornecedorEntity
            proposta.setCondicaoPagamento(produto.getCondicaoPagamento()); // Assumindo que você adicionará isso
            proposta.setObservacoes(produto.getObservacoes()); // Assumindo que você adicionará isso

            // Lógica do melhor preço
            if (produto.equals(melhorProduto)) {
                proposta.setMelhorPreco(true);
            }

            propostas.add(proposta);
        }

        propostaCotacaoRepository.saveAll(propostas);
        cotacao.setPropostas(propostas);
        return cotacao;
    }

    /**
     * Passo 2: O usuário visualiza as propostas (Tela MostrarCotacao.jsx)
     * MÉTODO ATUALIZADO para usar o Mapper
     */
    public List<PropostaCotacaoDTO> listarPropostas(Long cotacaoId) {
        List<PropostaCotacaoEntity> propostas = propostaCotacaoRepository.findByCotacaoId(cotacaoId);

        return propostas.stream()
                .map(cotacaoMapper::toPropostaDTO) // Delega a conversão para o Mapper
                .collect(Collectors.toList());
    }

    /**
     * Passo 3: O usuário clica em "Confirmar Pedido" (Tela MostrarCotacao.jsx)
     * ESTA É A INTEGRAÇÃO CRÍTICA.
     */
    @Transactional
    public void confirmarPedido(Long propostaId) {
        // 1. Achar a proposta escolhida
        PropostaCotacaoEntity proposta = propostaCotacaoRepository.findById(propostaId)
                .orElseThrow(() -> new RuntimeException("Proposta não encontrada: " + propostaId));
        CotacaoEntity cotacao = proposta.getCotacao();

        if (!"ABERTA".equals(cotacao.getStatus())) {
            throw new RuntimeException("Esta cotação não está mais aberta e não pode ser confirmada.");
        }

        // 2. Buscar o nome correto do fornecedor (lógica interna do serviço)
        FornecedorEntity fornecedor = proposta.getProdutoFornecedor().getFornecedor();
        String nomeFornecedor = getNomeDoFornecedor(fornecedor); // Usa o método auxiliar privado

        // 3. Criar a Movimentação Financeira (A Despesa)
        MovFinanceiraDTO movFinDTO = new MovFinanceiraDTO();
        movFinDTO.setTipo(TipoMovFinanceiro.DESPESA);
        movFinDTO.setCategoriaFinanceira(CategoriaFinanceira.COMPRA_MATERIAL);
        movFinDTO.setValor(proposta.getValorUnitario().multiply(cotacao.getQuantidade()));
        movFinDTO.setDataMovimento(LocalDate.now());
        movFinDTO.setDescricao("Compra cotação #" + cotacao.getId() + ": " + cotacao.getInsumo().getNome() + " de " + nomeFornecedor);
        movFinDTO.setObraId(cotacao.getObra().getId());
        movFinDTO.setInsumoId(cotacao.getInsumo().getId());
        movFinDTO.setFuncionarioResponsavelId(cotacao.getFuncionarioSolicitante().getId());

        movFinanceiroService.cadastrar(movFinDTO);

        // 4. Criar a Movimentação de Estoque (A Entrada)
        // A. Encontrar o Estoque da Obra
        EstoqueEntity estoqueDaObra = cotacao.getObra().getEstoque();
        if (estoqueDaObra == null) {
            throw new RuntimeException("A Obra '" + cotacao.getObra().getNomeObra() + "' não possui um estoque associado!");
        }

        // B. Criar o DTO da Movimentação de Estoque
        MovEstoqueDTO movEstDTO = new MovEstoqueDTO();
        movEstDTO.setTipoMov(TipoMov.ENTRADA);
        movEstDTO.setInsumoId(cotacao.getInsumo().getId());
        movEstDTO.setEstoqueDestinoId(estoqueDaObra.getId());
        movEstDTO.setQuantidade(cotacao.getQuantidade());
        movEstDTO.setFuncionarioId(cotacao.getFuncionarioSolicitante().getId());
        movEstDTO.setDataMovimentacao(LocalDate.now()); // Idealmente, esta data deveria ser a da *entrega*

        // C. Informar os dados do material para o estoque (preço, etc)
        MaterialEstoqueRequest matEstoqueRequest = new MaterialEstoqueRequest();
        matEstoqueRequest.setValor(proposta.getValorUnitario());
        // Aqui você pode definir valores padrão de min/max se desejar
        // matEstoqueRequest.setQuantidadeMinima(...);
        // matEstoqueRequest.setQuantidadeMaxima(...);

        movEstDTO.setMaterialEstoque(matEstoqueRequest);

        movEstoqueService.cadastrar(movEstDTO);

        // 5. Fechar a Cotação
        cotacao.setStatus("CONCLUIDA");
        cotacaoRepository.save(cotacao);
    }

    // --- MÉTODO AUXILIAR PRIVADO ---
    // (Necessário para a lógica de 'confirmarPedido' criar a descrição)

    /**
     * Busca o nome fantasia (se PJ) ou o nome (se PF) do fornecedor.
     */
    private String getNomeDoFornecedor(FornecedorEntity fornecedor) {
        if (fornecedor == null) return "Fornecedor N/A";

        if (fornecedor.getTipoPessoa() == TipoPessoa.JURIDICA) {
            UsuarioJuridico uj = usuarioJuridicoRepository.findByUsuarioId(fornecedor.getId());
            if (uj != null && uj.getNomeFantasia() != null && !uj.getNomeFantasia().isEmpty()) {
                return uj.getNomeFantasia();
            }
        }
        // Fallback para o nome base (Pessoa Física ou PJ sem nome fantasia)
        return fornecedor.getNome();
    }
}