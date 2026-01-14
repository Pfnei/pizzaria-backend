package at.incrustwetrust.pizzeria.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
        String message,
        String link,        // Der Link zur Registrierung
        LocalDateTime time  // Zeitstempel für die Fehlersuche
) {}