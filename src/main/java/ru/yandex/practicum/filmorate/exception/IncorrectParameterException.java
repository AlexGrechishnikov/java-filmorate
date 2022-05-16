package ru.yandex.practicum.filmorate.exception;

public class IncorrectParameterException extends RuntimeException {
    public IncorrectParameterException(String parameter) {
        super(String.format("Параметр запроса '%s' содержит недопустимое значение", parameter));
    }
}