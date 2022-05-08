package ru.yandex.practicum.filmorate.exception;

public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(Object obj) {
        super(String.format("Object %s is already exists", obj));
    }
}
