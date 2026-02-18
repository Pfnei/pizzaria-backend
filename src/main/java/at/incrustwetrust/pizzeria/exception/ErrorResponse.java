package at.incrustwetrust.pizzeria.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
        String message,
        String link,        // The link to registration
        LocalDateTime time  // Timestamp for debugging
) {}