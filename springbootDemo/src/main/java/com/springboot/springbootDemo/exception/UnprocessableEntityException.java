package com.springboot.springbootDemo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UnprocessableEntityException extends RuntimeException{
    public UnprocessableEntityException(String message){
        super(message);
    }
}
