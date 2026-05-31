package com.example.Almoxarifado.common.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import tools.jackson.databind.exc.InvalidFormatException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
    
    Map<String, String> errorsDetails = new HashMap<>();
    
    ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
        errorsDetails.put(fieldError.getField(), fieldError.getDefaultMessage());
    });

    Map<String, Object> body = new HashMap<>();
    body.put("timestamp", LocalDateTime.now().toString());
    body.put("status", HttpStatus.BAD_REQUEST.value());
    body.put("error", "Bad Request");
    body.put("message", errorsDetails); 
    body.put("path", "/chaves");

    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
}

@ExceptionHandler(HttpMessageNotReadableException.class)
public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
    Map<String, Object> body = new HashMap<>();
    body.put("timestamp", LocalDateTime.now().toString());
    body.put("status", HttpStatus.BAD_REQUEST.value());
    body.put("error", "Bad Request");
    
    String message = "Verifique a formatação dos campos.";
    
    if (ex.getCause() instanceof InvalidFormatException) {
        InvalidFormatException ife = (InvalidFormatException) ex.getCause();
        String campo = ife.getPath().isEmpty() ? "campo" : ife.getPath().get(0).getPropertyName();
        message = String.format("O campo '%s' espera um valor do tipo %s, mas recebeu '%s'.", 
            campo, ife.getTargetType().getSimpleName(), ife.getValue());
    }

    body.put("message", message);
    body.put("path", "/chaves");

    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
}
}