package at.incrustwetrust.pizzeria.exception;

public class OrderNotFoundException extends ResourceNotFoundException{
    public OrderNotFoundException(String message) {
        super(message);
    }


}
