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

    // 1. Spezifisch: Wenn ein User oder Produkt schon existiert (409)
    @ExceptionHandler({UserAlreadyExistsException.class, ProductAlreadyExistsException.class})
    public ResponseEntity<ErrorResponse> handleConflict(RuntimeException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), null);
    }

    // 2. Spezifisch: Wenn etwas nicht gefunden wurde (404)
    // Das fängt auch UserNotFoundException ab, da diese von ResourceNotFound erbt!
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        String link = (ex instanceof UserNotFoundException) ? "https://pizzeria.at/register" : null;
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), link);
    }

    // 3. Spezifisch: Sicherheitsfehler / Virenscanner (403)
    @ExceptionHandler(InsecureFileException.class)
    public ResponseEntity<ErrorResponse> handleInsecureFile(InsecureFileException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, ex.getMessage(), "https://pizzeria.at/security-policy");
    }

    // 4. Wichtig: Validierungsfehler (400)
    // Wenn z.B. @Valid im Controller fehlschlägt (z.B. E-Mail Format falsch)
    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(org.springframework.web.bind.MethodArgumentNotValidException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Eingabedaten sind ungültig.", null);
    }



    // Hilfsmethode, um den Code oben kurz zu halten
    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message, String link) {
        ErrorResponse error = new ErrorResponse(message, link, LocalDateTime.now());
        return ResponseEntity.status(status).body(error);
    }


    // 5. Globaler Catch-All: Für alles, was wir vergessen haben (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralError(Exception ex) {
        // 1. Logge den Fehler für dich (inklusive Stacktrace durch das 'ex')
        log.error("Kritischer Systemfehler: ", ex);

        // 2. Erstelle eine Antwort für den User (ohne technische Details!)
        ErrorResponse error = new ErrorResponse(
                "Ein unerwarteter Fehler ist aufgetreten. Bitte versuche es später erneut.",
                null,
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}