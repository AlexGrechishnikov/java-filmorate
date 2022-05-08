package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ValidationException;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Some parameters is invalid")
public class CustomValidationException extends ValidationException {
    public CustomValidationException(String message) {
        super(message);
    }
}
