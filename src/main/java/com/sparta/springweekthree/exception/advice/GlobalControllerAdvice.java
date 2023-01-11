package com.sparta.springweekthree.exception.advice;

import com.sparta.springweekthree.exception.dto.ExceptionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
//@RestControllerAdvice(basePackages = "com.sparta.springweekthree.member")
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ExceptionMessage> illegalArgumentExceptionExceptionHandle(IllegalArgumentException exception) {
        ExceptionMessage message = new ExceptionMessage(exception.getMessage(), BAD_REQUEST);
        return new ResponseEntity<>(message, message.getStatus());
    }
    @ExceptionHandler
    public ResponseEntity<ExceptionMessage> illegalAccessExceptionExceptionHandle(IllegalAccessException exception) {
        ExceptionMessage message = new ExceptionMessage(exception.getMessage(), UNAUTHORIZED);
        return new ResponseEntity<>(message, message.getStatus());
    }
}
