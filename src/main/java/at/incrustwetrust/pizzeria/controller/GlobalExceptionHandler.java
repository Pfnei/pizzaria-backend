package at.incrustwetrust.pizzeria.controller;

import at.incrustwetrust.pizzeria.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // 1. Specific: If a user or product already exists (409)
    @ExceptionHandler({UserAlreadyExistsException.class, ProductAlreadyExistsException.class})
    public ResponseEntity<ErrorResponse> handleConflict(RuntimeException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), null);
    }

    // 2. Specific: If something was not found (404)
    // This also catches UserNotFoundException, as it inherits from ResourceNotFound!
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        String link = (ex instanceof UserNotFoundException) ? "https://pizzeria.at/register" : null;
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), link);
    }

    // 3. Specific: Security error / Virus scanner (403)
    @ExceptionHandler(InsecureFileException.class)
    public ResponseEntity<ErrorResponse> handleInsecureFile(InsecureFileException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, ex.getMessage(), "https://pizzeria.at/security-policy");
    }

    // 3a. Spring Security: Access denied -> 403
    @ExceptionHandler({org.springframework.security.access.AccessDeniedException.class,
            org.springframework.security.authorization.AuthorizationDeniedException.class})
    public ResponseEntity<ErrorResponse> handleSpringAccessDenied(Exception ex) {
        return buildResponse(HttpStatus.FORBIDDEN, "Access Denied", "https://pizzeria.at/help/permissions");
    }

    // 4. Important: Validation error (400)
    // If e.g. @Valid fails in the controller (e.g. email format wrong)
    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(org.springframework.web.bind.MethodArgumentNotValidException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Input data is invalid.", null);
    }


    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedAction(UnauthorizedActionException ex) {
        // Which status code and which message do we assemble here?
        return buildResponse(
                HttpStatus.FORBIDDEN,
                ex.getMessage(),
                "https://pizzeria.at/help/permissions"
        );
    }

    @ExceptionHandler(UpdateFailedException.class)
        public ResponseEntity<ErrorResponse> handleUpdateFailAction(Exception ex){
            return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()+"update failed", null);
    }

    // Helper method to keep the code above short
    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message, String link) {
        ErrorResponse error = new ErrorResponse(message, link, LocalDateTime.now());
        return ResponseEntity.status(status).body(error);
    }


    // 5. Global Catch-All: For everything we forgot (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralError(Exception ex) {
        // 1. Log the error for you (including stacktrace through 'ex')
        log.error("Critical system error: ", ex);

        // 2. Create a response for the user (without technical details!)
        ErrorResponse error = new ErrorResponse(
                "An unexpected error occurred. Please try again later.",
                null,
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
