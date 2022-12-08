package com.malibu.pwc.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {PathNotFoundException.class, RuleException.class})
    protected ResponseEntity<Object> handlerRuleException(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, createExceptionResponse(ex),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    protected ResponseEntity<Object> handlerResourceNotFoundException(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, createExceptionResponse(ex),
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    private Map<String, String> createExceptionResponse(RuntimeException runtimeException) {
        Map<String, String> response = new HashMap<>();
        response.put("message", runtimeException.getMessage());
        return response;
    }
}
