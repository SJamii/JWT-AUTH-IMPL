package com.example.petProject.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ResourceCannotBeDeletedException extends RuntimeException {
    public ResourceCannotBeDeletedException(String message) {
        super(message);
    }
}
