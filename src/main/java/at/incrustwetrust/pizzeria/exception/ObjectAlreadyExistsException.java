package at.incrustwetrust.pizzeria.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ObjectAlreadyExistsException extends RuntimeException{
    public ObjectAlreadyExistsException() {
    }

    public ObjectAlreadyExistsException(String message) {
        super(message);
    }

    public ObjectAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ObjectAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public ObjectAlreadyExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
