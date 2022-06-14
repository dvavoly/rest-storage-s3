package org.example.storage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundByEmailException extends RuntimeException{
    public UserNotFoundByEmailException(String email) {
        super(String.format("Could not find user with email: %s", email));
    }
}
