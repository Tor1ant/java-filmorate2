package ru.yandex.practicum.filmorate.controller.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.model.Error;

@Slf4j
@RestControllerAdvice
public class Handler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Error handeException(final Exception e) {
        log.error("Выбросилось исключение ", e);
        return new Error(e.getMessage(), HttpStatusCode.valueOf(500).toString());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public Error handleNotFoundException(final NotFoundException e) {
        log.error("Выбросилось исключение 404", e);
        return new Error(e.getMessage(), HttpStatusCode.valueOf(404).toString());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public Error handleValidationException(final ValidationException e) {
        log.error("Выбросилось исключение 400", e);
        return new Error(e.getMessage(), HttpStatusCode.valueOf(400).toString());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Error handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("Выбросилось исключение 400", ex);
        return new Error(ex.getMessage(), HttpStatusCode.valueOf(400).toString());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public Error emptyResultDataAccessException(EmptyResultDataAccessException e) {
        log.error("Объект не найден в базе данных и выбросилось 404", e);
        String message = "Объект не найден";
        return new Error(message, HttpStatusCode.valueOf(404).toString());
    }
}
