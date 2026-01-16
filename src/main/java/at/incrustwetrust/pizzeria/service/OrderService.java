package at.incrustwetrust.pizzeria.service;

import at.incrustwetrust.pizzeria.dto.order.*;
import at.incrustwetrust.pizzeria.entity.Order;
import at.incrustwetrust.pizzeria.entity.User;
import at.incrustwetrust.pizzeria.exception.OrderNotFoundException;
import at.incrustwetrust.pizzeria.exception.UnauthorizedActionException;
import at.incrustwetrust.pizzeria.exception.UserNotFoundException;
import at.incrustwetrust.pizzeria.mapper.OrderMapper;
import at.incrustwetrust.pizzeria.repository.OrderRepository;
import at.incrustwetrust.pizzeria.repository.UserRepository;
import at.incrustwetrust.pizzeria.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;

    // READ ALL (optional filter)
    public List<OrderResponseDTO> readAll(Optional<String> createdBy, SecurityUser principal) {
        loggedInUserCheck(principal);

        if (principal.isAdmin()) {
            // Admin darf filtern oder alles sehen
            List<Order> orders = createdBy
                    .map(orderRepository::findAllByCreatedBy_UserId)
                    .orElseGet(orderRepository::findAll);
            return orderMapper.toResponseDtoList(orders);
        } else {
            // Normaler User:  ignorieren den Filter und nehmen eingeloggte ID vom principal
            return orderMapper.toResponseDtoList(
                    orderRepository.findAllByCreatedBy_UserId(principal.getId())
            );
        }
    }

    // READ ONE
    public OrderResponseDTO read(String orderId,SecurityUser principal) {
        if (principal == null) {
            throw new UnauthorizedActionException("your not logged in");
        }
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Keine Bestellung mit der ID " + orderId + " vorhanden"));

        if (principal.isAdmin()) {
            return orderMapper.toResponseDto(order);
        } else if (!principal.isAdmin()) {
            if (!principal.getId().equals(order.getCreatedBy().getUserId())) {
                throw new OrderNotFoundException("order not found");
            }
            return orderMapper.toResponseDto(order);
        }
        return null;
    }

    // CREATE
    public OrderResponseDTO create(OrderCreateDTO dto,SecurityUser principal) {

        User createdBy = null;
        if (principal != null) {
            createdBy = userRepository.findById(principal.getId())
                    .orElseThrow(() -> new UserNotFoundException("User nicht gefunden"));
        }

        Order orderToSave = orderMapper.toEntity(dto, createdBy);
        Order savedOrder = orderRepository.save(orderToSave);
        return orderMapper.toResponseDto(savedOrder);
    }

    // update muss noch gemacht werden, create, read, readalll sollten passen !
    public OrderResponseDTO update(String id, OrderUpdateDTO dto, SecurityUser principal) {
        // Prüfen ob eingeloggt
        loggedInUserCheck(principal);

        // Order suchen
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Keine Bestellung mit der ID " + id + " vorhanden"));

        // Berechtigung prüfen (Admin oder Besitzer)
        if (!principal.isAdmin() && !principal.getId().equals(order.getCreatedBy().getUserId())) {
            throw new OrderNotFoundException("Bestellung nicht gefunden"); // Tarnung als 'nicht gefunden'
        }

        //Mappen und speichern
        orderMapper.updateEntity(dto, order);
        Order saved = orderRepository.save(order);
        return orderMapper.toResponseDto(saved);
    }

    private void loggedInUserCheck(SecurityUser principal){
        if(principal == null){
            throw new UnauthorizedActionException("your not logged in");
        }
    }
}
