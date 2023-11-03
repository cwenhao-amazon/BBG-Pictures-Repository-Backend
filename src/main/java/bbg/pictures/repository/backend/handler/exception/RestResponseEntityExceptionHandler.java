package bbg.pictures.repository.backend.handler.exception;

import java.time.LocalDateTime;

import bbg.pictures.repository.backend.model.response.ErrorResponse;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = IllegalStateException.class)
    protected ResponseEntity<Object> handleIllegalStateException(final IllegalStateException ex,
                                                                 final WebRequest request) {
        final ErrorResponse responseBody = ErrorResponse.builder()
                                                        .timestamp(LocalDateTime.now())
                                                        .status(HttpStatus.CONFLICT.value())
                                                        .error(ex.getMessage())
                                                        .build();
        return handleExceptionInternal(ex, responseBody, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = {UnrecognizedPropertyException.class, IllegalArgumentException.class})
    protected ResponseEntity<Object> handleUnrecognizedPropertyException(final RuntimeException ex,
                                                                         final WebRequest request) {
        final ErrorResponse responseBody = ErrorResponse.builder()
                                                        .timestamp(LocalDateTime.now())
                                                        .status(HttpStatus.BAD_REQUEST.value())
                                                        .error(ex.getMessage())
                                                        .build();
        return handleExceptionInternal(ex, responseBody, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFoundException(final EntityNotFoundException ex,
                                                                   final WebRequest request) {
        final ErrorResponse responseBody = ErrorResponse.builder()
                                                        .timestamp(LocalDateTime.now())
                                                        .status(HttpStatus.NOT_FOUND.value())
                                                        .error(ex.getMessage())
                                                        .build();
        return handleExceptionInternal(ex, responseBody, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
}
