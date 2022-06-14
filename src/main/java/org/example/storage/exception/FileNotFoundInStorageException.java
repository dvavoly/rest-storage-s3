package org.example.storage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FileNotFoundInStorageException extends RuntimeException {
    public FileNotFoundInStorageException(Long id) {
        super(String.format("Could not find file: %d", id));
    }
}
