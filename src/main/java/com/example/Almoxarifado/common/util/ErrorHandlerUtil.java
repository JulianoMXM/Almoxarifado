package com.example.Almoxarifado.common.util;


import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ErrorHandlerUtil {

    public static ResponseStatusException handleError(String context, Exception error, Logger logger) {
        
        if (error instanceof ResponseStatusException) {
            throw (ResponseStatusException) error;
        }

        String message = (error != null && error.getMessage() != null) ? error.getMessage() : "Erro desconhecido";

        logger.error("[{}] error: {}", context, message, error);

        return new ResponseStatusException(
            HttpStatus.INTERNAL_SERVER_ERROR, 
            "Erro interno ao processar a operação em: " + context
        );
    }
}
