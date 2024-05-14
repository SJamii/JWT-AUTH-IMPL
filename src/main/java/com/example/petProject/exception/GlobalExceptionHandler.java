package com.example.petProject.exception;

import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;
import java.util.Objects;

import static com.example.petProject.utils.ApiResponse.error;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handling custom validation errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<JSONObject> customValidationErrorHandling(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(400)
                .body(
                        error("Input Validation Failed",
                                HttpStatus.BAD_REQUEST.value(),
                                Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage()).getJson());

    }

    /**
     * Handling bad credentials exception
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> customBadCredentialsExceptionHandling(BadCredentialsException ex) {
        ErrorResponse errorResponse = new ErrorResponse(new Date(), "Email or Password is INCORRECT!!!", ex.getMessage(), HttpStatus.UNAUTHORIZED.value());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value())
                .body(
                        error("Email or Password is INCORRECT!!!",
                                HttpStatus.UNAUTHORIZED.value(),
                                Objects.requireNonNull(ex.getMessage())).getJson());

    }

    /**
     * Handling access control exception
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> customAccessControlHandling(AccessDeniedException ex) {
        ErrorResponse errorResponse = new ErrorResponse(new Date(), "YOU ARE NOT AUTHORIZED TO PERFORM THIS OPERATION!!!", ex.getMessage(), HttpStatus.UNAUTHORIZED.value());
        return ResponseEntity.status(HttpStatus.FORBIDDEN.value())
                .body(
                        error("YOU ARE NOT AUTHORIZED TO PERFORM THIS OPERATION!!!",
                                HttpStatus.FORBIDDEN.value(),
                                Objects.requireNonNull(ex.getMessage())).getJson());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus
    public ResponseEntity<JSONObject> handleException(Exception ex) {
        return ResponseEntity.status(500)
                .body(error("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()).getJson());
    }
}
