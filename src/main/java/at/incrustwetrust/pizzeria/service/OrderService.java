package at.incrustwetrust.pizzeria.service;

import at.incrustwetrust.pizzeria.entity.Order;
import at.incrustwetrust.pizzeria.exception.ObjectAlreadyExistsException;
import at.incrustwetrust.pizzeria.exception.ObjectNotFoundException;
import at.incrustwetrust.pizzeria.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> readAll(){
        return orderRepository.findAll();
    }

    public Order read(String id){
        return orderRepository.findById(id).orElseThrow( () -> new ObjectNotFoundException("Kein Bestellung mit der ID " + id + " vorhanden"));
    }

    // How do I make sure that not the same order is places several times by pressing a button
    public Order create (Order order) {
        if (null != order.getOrderId() ) {
            throw new ObjectAlreadyExistsException(" Es gibt bereits eine Bestellung mit diese ID (" + order.getOrderId() + ") - Bestellungen können nicht geändert oder gelöscht werden");
        }
        return orderRepository.save(order);
    }



}
