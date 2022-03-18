package com.example.socialApp.shared;

import com.example.socialApp.exceptions.*;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@RestControllerAdvice
public class RestControllerExceptions {

   @ExceptionHandler(value = {TokenException.class, OperationAccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorMessage handleForbidden(RuntimeException exception, WebRequest request) {
        return new ErrorMessage(HttpStatus.FORBIDDEN.value(),
                new Date(),
                exception.getMessage(),
                request.getDescription(false));
    }

    @ExceptionHandler(value = {DuplicateObjectException.class, AgeLimitException.class,
            EventParticipantLimitException.class, DataIntegrityViolationException.class,
            OldPasswordMismatchException.class, NoSuchElementException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorMessage handleConflict(RuntimeException exception, WebRequest request) {
        if (exception instanceof DataIntegrityViolationException) {
            DataIntegrityViolationException dExp = (DataIntegrityViolationException) exception;
            return new ErrorMessage(HttpStatus.CONFLICT.value(),
                    new Date(),
                    Objects.requireNonNull(dExp.getRootCause()).getMessage(),
                    request.getDescription(false));
        } else {
            return new ErrorMessage(HttpStatus.CONFLICT.value(),
                    new Date(),
                    exception.getMessage(),
                    request.getDescription(false));
        }
    }

    @ExceptionHandler(value = {NullPointerException.class, EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleNull(RuntimeException exception, WebRequest request) {
        return new ErrorMessage(HttpStatus.NOT_FOUND.value(),
                new Date(),
                exception.getMessage(),
                request.getDescription(false));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult()
                .getAllErrors()
                .forEach((error) -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = FileUploadException.class)
    public ErrorMessage handleFileUploadExceptions(FileUploadException ex, WebRequest request) {
        return new ErrorMessage(HttpStatus.BAD_REQUEST.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ErrorMessage handleMaxSizeException(WebRequest request) {
        return new ErrorMessage(HttpStatus.PAYLOAD_TOO_LARGE.value(),
                new Date(),
                "File too large!",
                request.getDescription(false));
    }
}
