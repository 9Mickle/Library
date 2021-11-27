package com.example.library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserAlreadyHaveBook extends RuntimeException {
    public UserAlreadyHaveBook(String message) {
        super(message);
    }
}
