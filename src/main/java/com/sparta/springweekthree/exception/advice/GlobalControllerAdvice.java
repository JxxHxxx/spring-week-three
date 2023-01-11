package com.sparta.springweekthree.exception.advice;

import com.sparta.springweekthree.exception.dto.ExceptionMessage;
import com.sparta.springweekthree.exception.dto.ValidExceptionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ExceptionMessage> illegalArgumentExceptionExceptionHandle(IllegalArgumentException exception) {
        ExceptionMessage message = new ExceptionMessage(exception.getMessage(), BAD_REQUEST);
        return new ResponseEntity<>(message, HttpStatus.valueOf(message.getStatusCode()));
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionMessage> illegalAccessExceptionExceptionHandle(IllegalAccessException exception) {
        ExceptionMessage message = new ExceptionMessage(exception.getMessage(), UNAUTHORIZED);
        return new ResponseEntity<>(message, HttpStatus.valueOf(message.getStatusCode()));
    }

    @ExceptionHandler
    public ResponseEntity<ValidExceptionMessage> methodArgumentNotValidExceptionHandle(MethodArgumentNotValidException exception) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        List<String> errors = fieldErrors.stream().map(e -> e.getDefaultMessage()).collect(Collectors.toList());

        ValidExceptionMessage message = new ValidExceptionMessage(errors, BAD_REQUEST);
        return new ResponseEntity<>(message, HttpStatus.valueOf(message.getStatusCode()));
    }
}
