package at.incrustwetrust.pizzeria.exception;

public class UpdateFailedException extends RuntimeException{
    public UpdateFailedException(String message) {
        super(message);
    }
}
