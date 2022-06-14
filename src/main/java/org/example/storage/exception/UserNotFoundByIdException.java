package org.example.storage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundByIdException extends RuntimeException {

    public UserNotFoundByIdException(Long id) {
        super(String.format("Could not find user with id: %d", id));
    }
}
