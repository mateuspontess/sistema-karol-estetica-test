package br.com.karol.sistema.infra.exceptions;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;


@RestControllerAdvice
public class ErrorHandler {
    private static final String GENERIC_INPUT_VALIDATION_ERROR_MESSAGE = "Input validation error";

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleError404() {
        return ResponseEntity.notFound().build();
    }
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<?> handleError404(NoResourceFoundException ex) {
    	return ResponseEntity.notFound().build();
    }
	
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessageWithFields> handleError400(MethodArgumentNotValidException ex) {
    	Map<String, String> fields = ex.getFieldErrors().stream()
            .collect(Collectors.toMap(f -> 
                f.getField().toString(), 
                f -> f.getDefaultMessage()
            )
        );
    	return ResponseEntity
            .badRequest()
            .body(new ErrorMessageWithFields(
            GENERIC_INPUT_VALIDATION_ERROR_MESSAGE,
            fields)
        );
    }

    @ExceptionHandler(FieldValidationException.class)
    public ResponseEntity<ErrorMessageWithFields> handleError400(FieldValidationException ex) {
        Map<String, String> details = 
            Map.of(ex.getFieldError(), ex.getErrorMessage());
        return ResponseEntity
            .badRequest()
            .body(new ErrorMessageWithFields(
                    GENERIC_INPUT_VALIDATION_ERROR_MESSAGE, 
                    details
                )
            );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMessage> handleError400(IllegalArgumentException ex) {
        return ResponseEntity
            .badRequest()
            .body(new ErrorMessage(ex.getMessage()));
    }
    
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorMessage> handleError400(HttpRequestMethodNotSupportedException ex) {
        return ResponseEntity
            .badRequest()
            .body(new ErrorMessage(ex.getMessage()));
    }
    
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorMessage> handleError400(MissingServletRequestParameterException ex) {
        return ResponseEntity
            .badRequest()
            .body(new ErrorMessage(ex.getBody().getDetail()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorMessageWithParam> handleError400(MethodArgumentTypeMismatchException ex) {
        var field = Map.of(ex.getPropertyName(), "valor inválido");
        return ResponseEntity
            .badRequest()
            .body(new ErrorMessageWithParam(
                GENERIC_INPUT_VALIDATION_ERROR_MESSAGE, 
                field
            )
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorMessage> handleError400(HttpMessageNotReadableException ex) {
        return ResponseEntity
            .badRequest()
            .body(new ErrorMessage("Formato de JSON inválido"));
    }
    
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorMessage> handleError415(HttpMediaTypeNotSupportedException ex) {
        String unsupportedType = Optional.ofNullable(ex.getContentType())
            .map(mediaType -> mediaType.getType() + "/" + mediaType.getSubtype())
            .orElse("unknown");

        String supportedTypes = ex.getSupportedMediaTypes().stream()
            .map(mediaType -> mediaType.getType() + "/" + mediaType.getSubtype())
            .collect(Collectors.joining(", "));

        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
            .body(new ErrorMessage(String.format(
                "Unsupported media type '%s'. Supported media types are: %s", 
                unsupportedType, 
                supportedTypes)
            )
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorMessage> handleBadCredentialsError401() {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(new ErrorMessage("Invalid credentials"));
    }
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorMessage> handleAuthenticationError401() {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(new ErrorMessage("Authentication failed"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorMessage> handleError403(AccessDeniedException ex) {
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(new ErrorMessage(ex.getMessage()));
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleError500(Exception ex) {
    	ex.printStackTrace();
    	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorMessage("Internal server error"));
    }
    
    private record ErrorMessage(String error) {};
    private record ErrorMessageWithFields(String error, Object fields) {};
    private record ErrorMessageWithParam(String error, Object param) {};
}