// Exception/GlobalExceptionHandler.java
package br.com.engecore.Exception;

import br.com.engecore.DTO.ApiResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(Exception ex) {
        HashMap<String, String> response = new HashMap<>();
        response.put("erro", "Erro interno do servidor: " + ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {

        String mensagem = "Não é possível excluir este registro, pois ele está sendo usado por outra parte do sistema.";


        ApiResponse<Object> response = new ApiResponse<>(false, mensagem, null);

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
}
