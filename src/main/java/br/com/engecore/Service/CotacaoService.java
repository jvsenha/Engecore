package br.com.engecore.Service;

import br.com.engecore.DTO.*;
import br.com.engecore.Entity.*;
import br.com.engecore.Enum.CategoriaFinanceira;
import br.com.engecore.Enum.TipoMov;
import br.com.engecore.Enum.TipoMovFinanceiro;
import br.com.engecore.Enum.TipoPessoa;
import br.com.engecore.Mapper.CotacaoMapper;
import br.com.engecore.Repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    @Autowired private CotacaoMapper cotacaoMapper;

    /**
     * MÉTODO PÚBLICO (CREATE)
     * Recebe um DTO com vários itens e cria uma cotação para CADA item.
     * Retorna uma Lista de Cotações.
     */
    @Transactional
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public List<CotacaoEntity> solicitarCotacao(CotacaoRequestDTO dto) {

        // 1. Validar dados gerais (Obra e Funcionário)
        ObrasEntity obra = obrasRepository.findById(dto.getObraId())
                .orElseThrow(() -> new RuntimeException("Obra não encontrada: " + dto.getObraId()));

        FuncionarioEntity func = funcionarioRepository.findById(dto.getFuncionarioId())
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado: " + dto.getFuncionarioId()));

        if (dto.getItens() == null || dto.getItens().isEmpty()) {
            throw new RuntimeException("A lista de itens não pode estar vazia.");
        }

        List<CotacaoEntity> cotacoesCriadas = new ArrayList<>();

        // 2. Faz um loop por cada item da solicitação
        for (ItemCotacaoDTO item : dto.getItens()) {

            // 3. Valida o Insumo do item
            InsumoEntity insumo = insumoRepository.findById(item.getInsumoId())
                    .orElseThrow(() -> new RuntimeException("Insumo não encontrado: " + item.getInsumoId()));

            // 4. Chama a lógica de criação individual
            CotacaoEntity cotacaoIndividual = criarCotacaoIndividual(
                    obra,
                    insumo,
                    item.getQuantidade(),
                    dto.getDataNecessidade(),
                    dto.getPrioridade(),
                    func
            );
            cotacoesCriadas.add(cotacaoIndividual);
        }

        // 5. Retorna a lista de cotações que foram criadas
        return cotacoesCriadas;
    }

    /**
     * MÉTODO PRIVADO (Lógica de Criação)
     * Cria uma cotação e propostas automáticas para UM único insumo.
     */
    private CotacaoEntity criarCotacaoIndividual(ObrasEntity obra, InsumoEntity insumo, BigDecimal quantidade,
                                                 LocalDate dataNecessidade, String prioridade, FuncionarioEntity func) {

        // 1. Criar e salvar a Cotação principal
        CotacaoEntity cotacao = new CotacaoEntity();
        cotacao.setObra(obra);
        cotacao.setInsumo(insumo);
        cotacao.setQuantidade(quantidade);
        cotacao.setDataNecessidade(dataNecessidade);
        cotacao.setPrioridade(prioridade);
        cotacao.setFuncionarioSolicitante(func);
        cotacao.setStatus("ABERTA");
        cotacao.setPropostas(new ArrayList<>());
        cotacao = cotacaoRepository.save(cotacao);

        // 2. Gerar propostas automáticas (lógica idêntica à anterior)
        gerarPropostasAutomaticas(cotacao);

        return cotacao;
    }

    /**
     * READ (Listar): Lista todas as cotações criadas (retorna DTO leve).
     */
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public List<CotacaoListDTO> listarTodas() {
        List<CotacaoEntity> cotacoes = cotacaoRepository.findAll();
        return cotacoes.stream()
                .map(cotacaoMapper::toCotacaoListDTO)
                .collect(Collectors.toList());
    }

    /**
     * READ (Detalhes): Busca detalhes de uma cotação (retorna DTO de Detalhes).
     */
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public CotacaoDetalhesDTO detalhesCotacao(Long id) {
        CotacaoEntity cotacao = buscarCotacaoPorId(id);
        return cotacaoMapper.toDetalhesDTO(cotacao);
    }

    /**
     * UPDATE: Atualiza uma cotação.
     */
    @Transactional
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public CotacaoDetalhesDTO atualizarCotacao(Long id, CotacaoRequestDTO dto) {
        // TODO: A lógica de atualização precisa ser refeita para suportar o novo DTO
        // que recebe uma lista de itens.
        CotacaoEntity cotacao = buscarCotacaoPorId(id);
        return cotacaoMapper.toDetalhesDTO(cotacao);
    }

    /**
     * DELETE: Deleta uma cotação.
     */
    @Transactional
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public void deletarCotacao(Long id) {
        CotacaoEntity cotacao = buscarCotacaoPorId(id);
        if ("CONCLUIDA".equals(cotacao.getStatus())) {
            throw new RuntimeException("Não é possível deletar uma cotação já CONCLUÍDA.");
        }
        cotacaoRepository.delete(cotacao);
    }

    /**
     * AÇÃO: Re-executa a busca por fornecedores para uma cotação ABERTA.
     * Retorna a nova lista de PropostaCotacaoDTO.
     */
    @Transactional
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public List<PropostaCotacaoDTO> regerarPropostas(Long cotacaoId) {
        // 1. Busca
        CotacaoEntity cotacao = buscarCotacaoPorId(cotacaoId);

        // 2. Valida
        if (!"ABERTA".equals(cotacao.getStatus())) {
            throw new RuntimeException("Não é possível re-gerar propostas para uma cotação que não está ABERTA.");
        }

        // 3. Executa a lógica (isto já salva as propostas)
        gerarPropostasAutomaticas(cotacao);

        // 4. Retorna a nova lista de DTOs de propostas
        return listarPropostas(cotacaoId);
    }

    /**
     * READ (Propostas): O usuário visualiza as propostas.
     */
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication) or @securityService.isFornecedor(authentication)")
    public List<PropostaCotacaoDTO> listarPropostas(Long cotacaoId) {
        List<PropostaCotacaoEntity> propostas = propostaCotacaoRepository.findByCotacaoId(cotacaoId);
        return propostas.stream()
                .map(cotacaoMapper::toPropostaDTO)
                .collect(Collectors.toList());
    }

    /**
     * AÇÃO: O usuário clica em "Confirmar Pedido".
     */
    @Transactional
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public void confirmarPedido(Long propostaId) {
        PropostaCotacaoEntity proposta = propostaCotacaoRepository.findById(propostaId)
                .orElseThrow(() -> new RuntimeException("Proposta não encontrada: " + propostaId));
        CotacaoEntity cotacao = proposta.getCotacao();

        if (!"ABERTA".equals(cotacao.getStatus())) {
            throw new RuntimeException("Esta cotação não está mais aberta.");
        }

        FornecedorEntity fornecedor = proposta.getProdutoFornecedor().getFornecedor();
        String nomeFornecedor = getNomeDoFornecedor(fornecedor);

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

        EstoqueEntity estoqueDaObra = cotacao.getObra().getEstoque();
        if (estoqueDaObra == null) {
            throw new RuntimeException("A Obra '" + cotacao.getObra().getNomeObra() + "' não possui um estoque associado!");
        }

        MovEstoqueDTO movEstDTO = new MovEstoqueDTO();
        movEstDTO.setTipoMov(TipoMov.ENTRADA);
        movEstDTO.setInsumoId(cotacao.getInsumo().getId());
        movEstDTO.setEstoqueDestinoId(estoqueDaObra.getId());
        movEstDTO.setQuantidade(cotacao.getQuantidade());
        movEstDTO.setFuncionarioId(cotacao.getFuncionarioSolicitante().getId());
        movEstDTO.setDataMovimentacao(LocalDate.now());

        MaterialEstoqueRequest matEstoqueRequest = new MaterialEstoqueRequest();
        matEstoqueRequest.setValor(proposta.getValorUnitario());

        if (proposta.getProdutoFornecedor() == null) {
            throw new RuntimeException("Erro de dados: A proposta não está ligada a um ProdutoFornecedor.");
        }
        if (proposta.getProdutoFornecedor().getMarca() == null) {
            throw new RuntimeException("Erro de dados: O ProdutoFornecedor (ID: " + proposta.getProdutoFornecedor().getId() + ") não possui uma Marca associada.");
        }

        matEstoqueRequest.setMarcaId(proposta.getProdutoFornecedor().getMarca().getId());
        matEstoqueRequest.setModelo(proposta.getProdutoFornecedor().getModelo());
        movEstDTO.setMaterialEstoque(matEstoqueRequest);
        movEstoqueService.cadastrar(movEstDTO);

        cotacao.setStatus("CONCLUIDA");
        cotacaoRepository.save(cotacao);
    }

    // --- MÉTODOS AUXILIARES PRIVADOS ---

    /**
     * Busca a Entidade pelo ID (método auxiliar interno)
     */
    private CotacaoEntity buscarCotacaoPorId(Long id) {
        return cotacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cotação não encontrada: " + id));
    }

    /**
     * (Extraído): Lógica para encontrar fornecedores e gerar propostas.
     */
    private void gerarPropostasAutomaticas(CotacaoEntity cotacao) {
        // 1. Limpe a coleção gerenciada.
        // O 'orphanRemoval=true' e 'CascadeType.ALL' farão o trabalho de deletar do banco.
        if (cotacao.getPropostas() == null) {
            cotacao.setPropostas(new ArrayList<>());
        } else {
            cotacao.getPropostas().clear();
        }

        // 2. Busque os produtos (sua lógica existente)
        List<ProdutoFornecedorEntity> produtos = produtoFornecedorRepository.findByInsumoId(cotacao.getInsumo().getId());
        if (produtos.isEmpty()) {
            System.err.println("Nenhum fornecedor cadastrado para o insumo: " + cotacao.getInsumo().getNome());
            return; // A cotação ficará com 0 propostas, o que está correto.
        }

        // 3. Encontre o melhor produto (sua lógica existente)
        ProdutoFornecedorEntity melhorProduto = produtos.stream()
                .min((p1, p2) -> p1.getValor().compareTo(p2.getValor()))
                .orElse(null);

        // 4. Itere e ADICIONE à lista gerenciada (NÃO crie uma lista nova)
        for (ProdutoFornecedorEntity produto : produtos) {
            PropostaCotacaoEntity proposta = new PropostaCotacaoEntity();
            proposta.setCotacao(cotacao); // Linka o filho ao pai
            proposta.setProdutoFornecedor(produto);
            proposta.setValorUnitario(produto.getValor());
            proposta.setPrazoEntrega(produto.getPrazoEntrega());
            proposta.setCondicaoPagamento(produto.getCondicaoPagamento());
            proposta.setObservacoes(produto.getObservacoes());

            if (produto.equals(melhorProduto)) {
                proposta.setMelhorPreco(true);
            }

            // 5. Adiciona o novo item DIRETAMENTE na lista do pai
            cotacao.getPropostas().add(proposta);
        }

        // 6. REMOVA as linhas problemáticas.
        // propostaCotacaoRepository.saveAll(propostas); <-- Não é necessário, o Cascade fará isso.
        // cotacao.setPropostas(propostas); <-- Esta era a linha do ERRO.

        // Como o método é @Transactional, o Hibernate salvará as
        // mudanças na lista 'cotacao.getPropostas()' automaticamente.
    }

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
        return fornecedor.getNome();
    }
}