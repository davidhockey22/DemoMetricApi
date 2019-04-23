package com.ddouberley.metrics.controllers;

import com.ddouberley.metrics.exceptions.ResourceNotFoundException;
import com.ddouberley.metrics.exceptions.UniqueNameException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestReponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value
            = { ResourceNotFoundException.class })
    protected ResponseEntity<Object> handleNoResourceFound(
            RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "The associated metric could not be found!";
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value
            = { UniqueNameException.class })
    protected ResponseEntity<Object> handleUniqueNameException(
            RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "The metric name is already in use.";
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, request);
    }
}
