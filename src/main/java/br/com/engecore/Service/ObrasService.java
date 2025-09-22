package br.com.engecore.Service;

import br.com.engecore.DTO.ObrasDTO;
import br.com.engecore.DTO.UnidadeObrasRequest;
import br.com.engecore.Entity.ClienteEntity;
import br.com.engecore.Entity.FuncionarioEntity;
import br.com.engecore.Entity.ObrasEntity;
import br.com.engecore.Enum.FaixaRenda;
import br.com.engecore.Enum.ProgramaSocial;
import br.com.engecore.Enum.StatusConst;
import br.com.engecore.Enum.TipoObra;
import br.com.engecore.Mapper.FasesMapper;
import br.com.engecore.Mapper.ObrasMapper;
import br.com.engecore.Repository.ClienteRepository;
import br.com.engecore.Repository.FasesRepository;
import br.com.engecore.Repository.FuncionarioRepository;
import br.com.engecore.Repository.ObrasRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ObrasService {

    @Autowired
    private ObrasRepository obrasRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private FasesRepository fasesRepository;



    // ===================== FUNCIONALIDADES GERAIS =====================

    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    @Transactional
    public ObrasDTO cadastrar(ObrasDTO dto) {
        ObrasEntity obra = new ObrasEntity();

        // Setar informações básicas...
        obra.setNomeObra(dto.getNomeObra());
        obra.setEndereco(dto.getEndereco());
        obra.setStatusConst(dto.getStatus());
        obra.setTipo(dto.getTipo());

        // Cliente e responsável
        if (dto.getClienteId() != null) {
            obra.setCliente(clienteRepository.findById(dto.getClienteId())
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado")));
        }

        if (dto.getResponsavelId() != null) {
            obra.setResponsavel(funcionarioRepository.findById(dto.getResponsavelId())
                    .orElseThrow(() -> new RuntimeException("Responsável não encontrado")));
        }

        // Unidades, valores, datas...
        obra.setTotalUnidades(dto.getTotalUnidades());
        obra.setUnidadesConcluidas(dto.getUnidadesConcluidas());
        obra.setValorTotal(dto.getValorTotal());
        obra.setValorLiberado(dto.getValorLiberado());
        obra.setPagosFornecedores(dto.getPagosFornecedores());
        obra.setCustoPorUnidade(dto.getCustoPorUnidade());
        obra.setFaixaRenda(dto.getFaixaRenda());
        obra.setDocumentacaoAprovada(dto.getDocumentacaoAprovada());
        obra.setProgramaSocial(dto.getProgramaSocial());
        obra.setDataInicio(dto.getDataInicio());
        obra.setDataPrevistaConclusao(dto.getDataPrevistaConclusao());
        obra.setDataConclusaoReal(dto.getDataConclusaoReal());

        // Fases (importante: só setar, não salvar manualmente)
        if (dto.getFases() != null) {
            obra.setFases(FasesMapper.toEntityList(dto.getFases(), obra));
        }
        obrasRepository.save(obra); // Cascade salva as fases automaticamente
        return ObrasMapper.toDTO(obra);
    }

    //Atualiza obra existente (ADM ou FUNC)
    @Transactional
    public ObrasDTO atualizarPorAdmFuncionario(Long id, ObrasDTO dto) {
        ObrasEntity obra = obrasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Obra não encontrada!"));

        // Informações básicas
        obra.setNomeObra(dto.getNomeObra());
        obra.setEndereco(dto.getEndereco());
        obra.setStatusConst(dto.getStatus());
        obra.setTipo(dto.getTipo());

        // Cliente
        if (dto.getClienteId() != null) {
            ClienteEntity cliente = clienteRepository.findById(dto.getClienteId())
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
            obra.setCliente(cliente);
        }

        // Responsável
        if (dto.getResponsavelId() != null) {
            FuncionarioEntity responsavel = funcionarioRepository.findById(dto.getResponsavelId())
                    .orElseThrow(() -> new RuntimeException("Responsável não encontrado"));
            obra.setResponsavel(responsavel);
        }

        // Dados de unidades
        obra.setTotalUnidades(dto.getTotalUnidades());
        obra.setUnidadesConcluidas(dto.getUnidadesConcluidas());

        // Financeiro
        obra.setValorTotal(dto.getValorTotal());
        obra.setValorLiberado(dto.getValorLiberado());
        obra.setPagosFornecedores(dto.getPagosFornecedores());
        obra.setCustoPorUnidade(dto.getCustoPorUnidade());

        // Programas sociais
        obra.setFaixaRenda(dto.getFaixaRenda());
        obra.setDocumentacaoAprovada(dto.getDocumentacaoAprovada());
        obra.setProgramaSocial(dto.getProgramaSocial());

        // Datas
        obra.setDataInicio(dto.getDataInicio());
        obra.setDataPrevistaConclusao(dto.getDataPrevistaConclusao());
        obra.setDataConclusaoReal(dto.getDataConclusaoReal());

        // Fases: criar nova lista mutável
        if (dto.getFases() != null) {
            obra.getFases().clear(); // remove fases antigas
            obra.getFases().addAll(FasesMapper.toEntityList(dto.getFases(), obra));
        }

        obrasRepository.saveAndFlush(obra);
        return ObrasMapper.toDTO(obra);
    }


    //Lista obras com filtros opcionais (status, cliente, responsável, tipo, faixa de renda)
    public List<ObrasDTO> listar(Optional<StatusConst> status,
                                      Optional<Long> clienteId,
                                      Optional<Long> responsavelId,
                                      Optional<TipoObra> tipo,
                                      Optional<FaixaRenda> faixaRenda) {

        // Começamos com uma Specification neutra (cb.conjunction() = "true")
        Specification<ObrasEntity> spec = (root, query, cb) -> cb.conjunction();

        if (status.isPresent()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("statusConst"), status.get()));
        }

        if (clienteId.isPresent()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("cliente").get("idCliente"), clienteId.get()));
        }

        if (responsavelId.isPresent()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("responsavel").get("idUsuario"), responsavelId.get()));
        }

        if (tipo.isPresent()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("tipo"), tipo.get()));
        }

        if (faixaRenda.isPresent()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("faixaRenda"), faixaRenda.get()));
        }

        // Executa a consulta dinâmica
        List<ObrasEntity> obras = obrasRepository.findAll(spec);

        // Converte para DTO
        return obras.stream()
                .map(ObrasMapper::toDTO)
                .collect(Collectors.toList());
    }

    //Retorna detalhes completos de uma obra

    public ObrasDTO detalhesObra(Long id) {
        ObrasEntity obra = obrasRepository.findById(id).orElseThrow(() -> new RuntimeException("Obra não encontrada!"));
        return ObrasMapper.toDTO(obra);
    }

    //Deleta uma obra (ADM ou FUNC responsável)
    @Transactional
    public void deletarObra(Long id) {
        ObrasEntity obra = obrasRepository.findById(id).orElseThrow(() -> new RuntimeException("Obra não encontrada!"));
        obrasRepository.delete(obra);
    }

    // ===================== FUNCIONALIDADES OBRAS SOCIAIS =====================//

    //Atualiza o número total de unidades e unidades concluídas
    @Transactional
    public ObrasDTO atualizarUnidades(Long id, UnidadeObrasRequest dto) {
        ObrasEntity obra = obrasRepository.findById(id).orElseThrow(() -> new RuntimeException("Obra não encontrada"));

        dto.getTotalUnidades().ifPresent(obra::setTotalUnidades);
        dto.getUnidadesConcluidas().ifPresent(obra::setUnidadesConcluidas);

        ObrasEntity obraAtualizada = obrasRepository.save(obra);

        return ObrasMapper.toDTO(obraAtualizada);
    }

    //Lista obras filtradas por programa social
    public List<ObrasDTO> listarObrasPorPrograma(ProgramaSocial programaSocial) {
        return obrasRepository.findByProgramaSocial(programaSocial).stream().map(ObrasMapper::toDTO).collect(Collectors.toList());
    }

    //Valida documentação ou convênios necessários
    @Transactional
    public ObrasDTO validarDocumentacao(Long id, boolean aprovado) {
        ObrasEntity obra = buscarObras(id);
        obra.setDocumentacaoAprovada(aprovado);

        return ObrasMapper.toDTO((obra));
    }

    //Gera relatório de progresso da obra (percentual de conclusão, status por bloco/fase)
    public String gerarRelatorioProgresso(Long id) {
        //devolcer um relatorio com cada fase( oque data do inicio da fase, dia termino, tempo previsto, tempo levado, descrição)
        String response = null;
        return response;
    }

    //Atualiza informações financeiras da obra (valores liberados, pagamentos, custos por unidade)
    //public ObrasDTO atualizarFinanceiro(Long id, double valorLiberado, double pagosFornecedores, double custoPorUnidade) {
    //vou ter que verificar a movimentação financeira relaciona a obras, receitas e despesas, e atualizar os paramentros
    //}

    public ObrasEntity buscarObras(Long id) {
        return obrasRepository.findById(id).orElseThrow(() -> new RuntimeException("Obra não encontrada"));
    }

    public BigDecimal calcularCustoPorUnidade(Integer totalUnidades, BigDecimal valorTotal) {
        if (totalUnidades == null || totalUnidades == 0 || valorTotal == null) {
            throw new IllegalArgumentException("Total de unidades e valor total devem ser válidos");
        }

        return valorTotal.divide(new BigDecimal(totalUnidades), 2, RoundingMode.HALF_UP);
    }

}
