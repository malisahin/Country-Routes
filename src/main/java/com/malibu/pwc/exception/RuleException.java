package com.malibu.pwc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RuleException extends RuntimeException {

    public RuleException(String message) {
        super(message);
    }
}
