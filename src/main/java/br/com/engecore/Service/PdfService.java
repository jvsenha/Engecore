package br.com.engecore.Service;

import br.com.engecore.DTO.AcompanhamentoFaseDTO;
import br.com.engecore.DTO.CurvaAbcDTO;
import br.com.engecore.DTO.DreObraDTO;
import br.com.engecore.DTO.MaterialEstoqueResponse;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
public class PdfService {

    @Autowired
    private ResourceLoader resourceLoader;

    private static final Locale BRASIL_LOCALE = new Locale("pt", "BR");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // ===================================================================================
    // MÉTODO PÚBLICO 1: GERAR DRE DA OBRA
    // ===================================================================================
    public byte[] gerarPdfDreHtml(DreObraDTO dto) throws IOException {
        String html = carregarTemplateHtml("dre_template.html");

        // Preencher placeholders
        html = html.replace("{{nomeObra}}", dto.getNomeObra() + " (ID: " + dto.getObraId() + ")");
        html = html.replace("{{statusObra}}", dto.getStatusObra());
        html = html.replace("{{dataGeracao}}", LocalDate.now().format(DATE_FORMATTER));
        html = html.replace("{{valorContratado}}", formatarMoeda(dto.getValorContratado()));
        html = html.replace("{{custoTotalRealizado}}", formatarMoeda(dto.getCustoTotalRealizado()));
        html = html.replace("{{saldo}}", formatarMoeda(dto.getSaldo()));

        // Lógica de cor condicional
        boolean isLucro = dto.getSaldo().compareTo(BigDecimal.ZERO) >= 0;
        html = html.replace("{{corSaldo}}", isLucro ? "black" : "red");

        return converterHtmlParaPdf(html, "relatorios/");
    }

    // ===================================================================================
    // MÉTODO PÚBLICO 2: GERAR ESTOQUE CRÍTICO
    // ===================================================================================
    public byte[] gerarPdfEstoqueCriticoHtml(List<MaterialEstoqueResponse> dtos) throws IOException {
        String html = carregarTemplateHtml("estoque_critico_template.html");

        // Gerar as linhas da tabela dinamicamente
        StringBuilder sbRows = new StringBuilder();
        String estoqueAtual = "";

        for (MaterialEstoqueResponse item : dtos) {
            // Adiciona um cabeçalho de grupo quando o nome do estoque mudar
            if (!item.getEstoque().equals(estoqueAtual)) {
                estoqueAtual = item.getEstoque();
                sbRows.append("<tr class='group-header'><td colspan='3'>")
                        .append(escapeHtml(estoqueAtual))
                        .append("</td></tr>");
            }

            // Adiciona a linha do item
            sbRows.append("<tr class='item-row'>")
                    .append("<td class='material'>").append(escapeHtml(item.getMaterial())).append("</td>")
                    .append("<td style='text-align: right;' class='qty-atual'>").append(formatarNumero(item.getQuantidadeAtual())).append("</td>")
                    .append("<td style='text-align: right;'>").append(formatarNumero(item.getQuantidadeMinima())).append("</td>")
                    .append("</tr>");
        }

        // Preencher placeholders
        html = html.replace("{{dataGeracao}}", LocalDate.now().format(DATE_FORMATTER));
        html = html.replace("{{tableRows}}", sbRows.toString());
        html = html.replace("{{totalItens}}", String.valueOf(dtos.size()));

        return converterHtmlParaPdf(html, "relatorios/");
    }

    // ===================================================================================
    // MÉTODO PÚBLICO 3: GERAR CURVA ABC
    // ===================================================================================
    public byte[] gerarPdfCurvaAbcHtml(List<CurvaAbcDTO> dtos) throws IOException {
        String html = carregarTemplateHtml("curva_abc_template.html");

        StringBuilder sbGrafico = new StringBuilder();
        StringBuilder sbTabela = new StringBuilder();
        String classeAtual = "";

        // Pega o maior valor para normalizar o gráfico de barras (primeiro item da lista)
        BigDecimal maxValor = dtos.isEmpty() ? BigDecimal.ONE : dtos.get(0).getCustoTotal();
        if(maxValor.compareTo(BigDecimal.ZERO) == 0) maxValor = BigDecimal.ONE;

        // Limita o gráfico aos 5 primeiros
        int countGrafico = 0;

        for (CurvaAbcDTO item : dtos) {
            // 1. Monta o Gráfico de Barras (Top 5)
            if (countGrafico < 5) {
                double percLargura = item.getCustoTotal().divide(maxValor, 4, BigDecimal.ROUND_HALF_UP).doubleValue() * 100;
                sbGrafico.append("<div class='chart-row'>")
                        .append("<span class='chart-label'>").append(escapeHtml(item.getInsumoNome())).append("</span>")
                        .append("<div class='chart-bar-bg'><div class='chart-bar' style='width: ").append(percLargura).append("%;'></div></div>")
                        .append("<span class='chart-value'>").append(formatarMoeda(item.getCustoTotal())).append("</span>")
                        .append("</div>");
                countGrafico++;
            }

            // 2. Monta a Tabela (Todas as classes)
            String proximaClasse = getClasseAbc(item.getPercentualAcumulado());
            if (!proximaClasse.equals(classeAtual)) {
                classeAtual = proximaClasse;
                sbTabela.append("<tr class='group-header'><td colspan='4'>").append(classeAtual).append("</td></tr>");
            }

            sbTabela.append("<tr class='item-row'>")
                    .append("<td>").append(escapeHtml(item.getInsumoNome())).append("</td>")
                    .append("<td style='text-align: right;'>").append(formatarMoeda(item.getCustoTotal())).append("</td>")
                    .append("<td style='text-align: right;'>").append(String.format(BRASIL_LOCALE, "%.2f%%", item.getPercentual())).append("</td>")
                    .append("<td style='text-align: right;'>").append(String.format(BRASIL_LOCALE, "%.2f%%", item.getPercentualAcumulado())).append("</td>")
                    .append("</tr>");
        }

        // Preencher placeholders
        html = html.replace("{{dataGeracao}}", LocalDate.now().format(DATE_FORMATTER));
        html = html.replace("{{graficoBarras}}", sbGrafico.toString());
        html = html.replace("{{tabelaClasses}}", sbTabela.toString());

        return converterHtmlParaPdf(html, "relatorios/");
    }

    // ===================================================================================
    // MÉTODO PÚBLICO 4: GERAR ACOMPANHAMENTO DE FASES
    // ===================================================================================
    public byte[] gerarPdfAcompanhamentoFasesHtml(List<AcompanhamentoFaseDTO> dtos, String nomeObra) throws IOException {
        String html = carregarTemplateHtml("fases_acompanhamento_template.html");

        // Gerar as linhas da tabela dinamicamente
        StringBuilder sbRows = new StringBuilder();
        for (AcompanhamentoFaseDTO fase : dtos) {
            sbRows.append("<tr class='item-row'>")
                    .append("<td>").append(getIconeStatusFase(fase.getStatusPrazo())).append(" ").append(escapeHtml(fase.getNomeFase())).append("</td>")
                    .append("<td>").append(escapeHtml(fase.getStatusPrazo())).append("</td>")
                    .append("<td style='text-align: center;'>").append(formatarData(fase.getDataInicio())).append("</td>")
                    .append("<td style='text-align: center;'>").append(formatarData(fase.getDataPrevistaTermino())).append("</td>")
                    .append("<td style='text-align: center;'>").append(formatarData(fase.getDataTerminoReal())).append("</td>")
                    .append("</tr>");
        }

        // Preencher placeholders
        html = html.replace("{{dataGeracao}}", LocalDate.now().format(DATE_FORMATTER));
        html = html.replace("{{nomeObra}}", escapeHtml(nomeObra));
        html = html.replace("{{tableRows}}", sbRows.toString());

        return converterHtmlParaPdf(html, "relatorios/");
    }


    // ===================================================================================
    // MÉTODOS AUXILIARES PRIVADOS
    // ===================================================================================

    /**
     * Carrega um template HTML do classpath.
     */
    private String carregarTemplateHtml(String templateNome) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:relatorios/" + templateNome);
        try (InputStream inputStream = resource.getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    /**
     * Converte uma string HTML preenchida em um PDF (byte[]).
     */
    private byte[] converterHtmlParaPdf(String html, String baseUri) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.withHtmlContent(html, resourceLoader.getResource("classpath:" + baseUri).getURL().toString());
        builder.toStream(baos);
        builder.run();
        return baos.toByteArray();
    }

    /**
     * Formata um BigDecimal para R$ 0.000,00.
     */
    private String formatarMoeda(BigDecimal valor) {
        if (valor == null) valor = BigDecimal.ZERO;
        return NumberFormat.getCurrencyInstance(BRASIL_LOCALE).format(valor);
    }

    /**
     * Formata um BigDecimal para um número (ex: 1.000,00).
     */
    private String formatarNumero(BigDecimal valor) {
        if (valor == null) valor = BigDecimal.ZERO;
        return NumberFormat.getNumberInstance(BRASIL_LOCALE).format(valor);
    }

    /**
     * Formata uma LocalDate para dd/MM/yyyy ou retorna "-".
     */
    private String formatarData(LocalDate data) {
        if (data == null) return "-";
        return data.format(DATE_FORMATTER);
    }

    /**
     * Define a classe ABC com base no percentual acumulado.
     */
    private String getClasseAbc(double percentualAcumulado) {
        if (percentualAcumulado <= 80.0) return "CLASSE A (Top 80% do Custo)";
        if (percentualAcumulado <= 95.0) return "CLASSE B (Próximos 15% do Custo)";
        return "CLASSE C (Últimos 5% do Custo)";
    }

    /**
     * Retorna um ícone HTML para o status da fase.
     */
    private String getIconeStatusFase(String status) {
        return switch (status) {
            case "Concluída", "Concluída com Atraso" ->
                    "<span class='status-symbol'>✓</span>";
            case "Atrasada" ->
                    "<span class='status-symbol status-late'>!</span>";
            case "Em Dia" ->
                    "<span class='status-symbol' style='line-height: 16px; font-family: monospace;'>...</span>";
            default -> // "Planejada", "Indefinido"
                    "<span class='status-symbol status-planned'> </span>";
        };
    }

    /**
     * Simples "escape" de HTML para evitar que dados quebrem o layout.
     */
    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}