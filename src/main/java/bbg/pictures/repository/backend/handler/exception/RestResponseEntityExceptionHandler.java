package bbg.pictures.repository.backend.handler.exception;

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
        final String responseBody = ex.getMessage();
        return handleExceptionInternal(ex, responseBody, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = UnrecognizedPropertyException.class)
    protected ResponseEntity<Object> handleUnrecognizedPropertyException(final UnrecognizedPropertyException ex,
                                                                         final WebRequest request) {
        final String responseBody = ex.getMessage();
        return handleExceptionInternal(ex, responseBody, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFoundException(final EntityNotFoundException ex,
                                                                   final WebRequest request) {
        final String responseBody = ex.getMessage();
        return handleExceptionInternal(ex, responseBody, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
}
