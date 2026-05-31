package com.example.Almoxarifado.common.util;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import tools.jackson.databind.exc.InvalidFormatException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Map<String, Object>> montarRespostaErro(HttpStatus status, String erro, Object mensagem, WebRequest request) {
            Map<String, Object> body = new HashMap<>();
            body.put("timestamp", LocalDateTime.now().toString());
            body.put("status", status.value());
            body.put("error", erro);
            body.put("message", mensagem);
            
            String path = request.getDescription(false).replace("uri=", "");
            body.put("path", path);

            return new ResponseEntity<>(body, status);
        }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        
        Map<String, String> errorsDetails = new HashMap<>();
        
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            errorsDetails.put(fieldError.getField(), fieldError.getDefaultMessage());
        });

        return montarRespostaErro(
            HttpStatus.BAD_REQUEST, 
            "Bad Request", 
            errorsDetails, 
            request
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
        
        String message = "Verifique a formatação dos campos.";
        
        if (ex.getCause() instanceof InvalidFormatException) {
            InvalidFormatException ife = (InvalidFormatException) ex.getCause();
            String campo = ife.getPath().isEmpty() ? "campo" : ife.getPath().get(0).getPropertyName();
            message = String.format("O campo '%s' espera um valor do tipo %s, mas recebeu '%s'.", 
                campo, ife.getTargetType().getSimpleName(), ife.getValue());
        }

        return montarRespostaErro(
            HttpStatus.BAD_REQUEST, 
            "Bad Request", 
            message, 
            request
        );
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundException(NotFoundException ex, WebRequest request) {
        return montarRespostaErro(
            HttpStatus.NOT_FOUND, 
            "Not Found", 
            "O registro solicitado não foi encontrado no sistema.", 
            request
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
        String mensagem = String.format("O parâmetro '%s' deve ser do tipo %s.", ex.getName(), ex.getRequiredType().getSimpleName());
        return montarRespostaErro(
            HttpStatus.BAD_REQUEST, 
            "Bad Request", 
            mensagem, 
            request
        );
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatus(ResponseStatusException ex, WebRequest request) {
        return montarRespostaErro(
            (HttpStatus) ex.getStatusCode(), 
            ((HttpStatus) ex.getStatusCode()).getReasonPhrase(), 
            ex.getReason(), 
            request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrity(DataIntegrityViolationException ex, WebRequest request) {
        return montarRespostaErro(
            HttpStatus.CONFLICT, 
            "Conflict", 
            "Operação violou a integridade dos dados no banco.", 
            request
        );
    }
}