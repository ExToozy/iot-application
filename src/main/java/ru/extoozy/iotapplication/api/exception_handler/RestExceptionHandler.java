package ru.extoozy.iotapplication.api.exception_handler;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.extoozy.iotapplication.api.dto.ErrorDto;
import ru.extoozy.iotapplication.api.exception.AlreadyExistsException;
import ru.extoozy.iotapplication.api.exception.NotFoundException;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto validationError(MethodArgumentNotValidException e) {
        Map<String, String> errors = e
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                                FieldError::getField, FieldError::getDefaultMessage
                        )
                );

        return ErrorDto.builder()
                .message("Validation failed")
                .errors(errors)
                .build();

    }

    @ExceptionHandler(AlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDto alreadyExistsException(AlreadyExistsException e) {
        return ErrorDto.builder()
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto notFoundException(NotFoundException e) {
        return ErrorDto.builder()
                .message(e.getMessage())
                .build();
    }
}
