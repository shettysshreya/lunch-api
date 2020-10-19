package com.task.lunch.exception;

import com.task.lunch.model.LunchApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Custom exception handler
 */
@RestControllerAdvice
public class LunchApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<LunchApiErrorResponse> handleValidationException(MethodArgumentNotValidException ex){
        LunchApiErrorResponse error = LunchApiErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage()).build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(LunchApiException.class)
    public ResponseEntity<LunchApiErrorResponse> handleLunchApiException(LunchApiException ex){
        System.out.println("handleLunchApiException");
        HttpStatus status = ex.getMessage().startsWith("Invalid request param")?
                HttpStatus.BAD_REQUEST: HttpStatus.INTERNAL_SERVER_ERROR;
        LunchApiErrorResponse error = LunchApiErrorResponse.builder()
                .status(status.value())
                .message(ex.getMessage()).build();
        return new ResponseEntity<>(error, status);
    }

}
