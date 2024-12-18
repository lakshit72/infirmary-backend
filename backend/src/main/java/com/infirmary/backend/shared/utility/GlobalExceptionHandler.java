package com.infirmary.backend.shared.utility;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @SuppressWarnings("null")
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", status.value());
        response.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        String defaultMessage = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        String message = defaultMessage.contains(":") ? defaultMessage.substring(defaultMessage.indexOf(":")) : defaultMessage;
        response.put("message", message);
        response.put("details", "Invalid Arguments");

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    // Handle 400 Bad Request
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleBadRequest(Exception ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Bad Request", ex);
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<?> handleSqlError(Exception ex){
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Error", ex);
    }

    // Handle JWT Expiration
    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<?> handleExpiredJWT(Exception ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "JWT Expired", ex);
    }

    // Handle 401 Unauthorized
    @ExceptionHandler(SecurityException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> handleUnauthorized(Exception ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Unauthorized", ex);
    }

    // Handle 403 Forbidden
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Object> handleForbidden(Exception ex) {
        return buildResponse(HttpStatus.FORBIDDEN, "Forbidden", ex);
    }

    // Handle 404 Not Found
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleNotFound(Exception ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "Not Found", ex);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> userNotFound(Exception ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "User Not Found", ex);
    }

    // Handle 500 Internal Server Error
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleInternalServerError(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", ex);
    }

    // Utility function to build the response for exceptions
    private ResponseEntity<Object> buildResponse(HttpStatus status, String message, Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", status.value());
        response.put("error", status.getReasonPhrase());
        response.put("message", ex.getMessage());
        response.put("details", message);

        return new ResponseEntity<>(response, status);
    }
}
