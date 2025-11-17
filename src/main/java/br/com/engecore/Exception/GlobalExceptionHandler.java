package br.com.engecore.Exception;

import br.com.engecore.DTO.ApiResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// NOVAS IMPORTAÇÕES (necessárias para o novo handler)
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;
// FIM DAS NOVAS IMPORTAÇÕES

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFound(ResourceNotFoundException ex) {
        HashMap<String, String> response = new HashMap<>();
        response.put("erro", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustomException(CustomException ex) {
        HashMap<String, String> response = new HashMap<>();
        response.put("erro", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // HANDLER PARA ERROS DE VALIDAÇÃO (@Pattern, @NotBlank, etc.)
    /**
     * Captura erros de validação do Jakarta (ex: @Pattern, @NotBlank, @Email).
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleConstraintViolation(ConstraintViolationException ex) {

        // Pega todas as mensagens de violação e as une com ", "
        // Ex: "Telefone deve estar no formato (99)..., Nome é obrigatório"
        String mensagem = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));

        // Se a mensagem estiver em branco por algum motivo, usa um fallback
        if (mensagem.isBlank()) {
            mensagem = "Erro de validação. Verifique os campos preenchidos.";
        }

        ApiResponse<Object> response = new ApiResponse<>(false, mensagem, null);

        // Erros de validação são 400 Bad Request
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    /**
     * Captura violações de integridade do banco (chaves únicas, chaves estrangeiras).
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {

        String mensagem = "Erro de integridade de dados. A operação não pôde ser concluída.";

        // Pega a causa raiz da exceção, que contém a mensagem do banco de dados
        String rootMsg = ex.getMostSpecificCause().getMessage().toLowerCase();

        // 1. Tenta identificar se é um erro de CHAVE ESTRANGEIRA (ex: tentando excluir um pai)
        if (rootMsg.contains("foreign key constraint fails") || rootMsg.contains("violates foreign key constraint")) {
            mensagem = "Não é possível excluir este registro, pois ele está sendo usado por outra parte do sistema (ex: uma obra, cotação ou produto).";

            // 2. Tenta identificar se é um erro de CHAVE ÚNICA (ex: duplicidade)
        } else if (rootMsg.contains("duplicate entry")) {

            // Nomes das constraints (podem variar, mas baseado nos logs anteriores)
            // Lembre-se que o usuário não verá os nomes 'UK...'

            // Constraint do Email (baseado no seu log)
            // uk5171l57faosmj8myawaucatdw -> 'email'
            if (rootMsg.contains("uk5171l57faosmj8myawaucatdw")) {
                String valor = extrairValorDuplicado(rootMsg);
                mensagem = String.format("O Email %s já está sendo usado por outro usuário.", valor);

                // Constraint do Telefone (baseado em logs anteriores)
                // uk86phslelq64eeo6insr50y422 -> 'telefone'
            } else if (rootMsg.contains("uk86phslelq64eeo6insr50y422")) {
                String valor = extrairValorDuplicado(rootMsg);
                mensagem = String.format("O Telefone %s já está sendo usado por outro usuário.", valor);

                // Fallback para outras constraints de duplicidade
            } else if (rootMsg.contains("cpf")) { // Se a constraint tiver 'cpf' no nome
                String valor = extrairValorDuplicado(rootMsg);
                mensagem = String.format("O CPF %s já está sendo usado por outro usuário.", valor);

            } else if (rootMsg.contains("cnpj")) { // Se a constraint tiver 'cnpj' no nome
                String valor = extrairValorDuplicado(rootMsg);
                mensagem = String.format("O CNPJ %s já está sendo usado por outro usuário.", valor);

            } else {
                // Mensagem genérica para duplicidade
                String valor = extrairValorDuplicado(rootMsg);
                mensagem = String.format("O valor %s já existe no sistema e não pode ser duplicado.", valor);
            }
        }

        ApiResponse<Object> response = new ApiResponse<>(false, mensagem, null);
        // 409 Conflict é o status HTTP mais apropriado para este tipo de erro
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    /**
     * Método auxiliar para tentar extrair o valor duplicado da mensagem de erro.
     * Ex: "Duplicate entry 'valor' for key..." -> retorna "'valor'"
     */
    private String extrairValorDuplicado(String rootMsg) {
        try {
            // Usa regex para encontrar o texto entre aspas simples
            Pattern pattern = Pattern.compile("'(.*?)'");
            Matcher matcher = pattern.matcher(rootMsg);
            if (matcher.find()) {
                // Retorna o valor encontrado, com as aspas
                return "'" + matcher.group(1) + "'";
            }
            return "informado"; // Fallback se não encontrar
        } catch (Exception e) {
            return "informado"; // Fallback em caso de erro na regex
        }
    }

    // O @ExceptionHandler(Exception.class) deve ser o último
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(Exception ex) {
        HashMap<String, String> response = new HashMap<>();
        response.put("erro", "Erro interno do servidor: " + ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}