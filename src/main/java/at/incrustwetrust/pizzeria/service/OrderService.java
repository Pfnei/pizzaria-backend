package at.incrustwetrust.pizzeria.service;

import at.incrustwetrust.pizzeria.dto.order.*;
import at.incrustwetrust.pizzeria.entity.Order;
import at.incrustwetrust.pizzeria.entity.User;
import at.incrustwetrust.pizzeria.mapper.OrderMapper;
import at.incrustwetrust.pizzeria.repository.OrderRepository;
import at.incrustwetrust.pizzeria.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;

    // READ ALL (optional filter)
    public List<OrderResponseLightDTO> readAll(Optional<String> createdBy) {
        List<Order> orders = createdBy
                .map(orderRepository::findAllByCreatedBy_UserId)
                .orElseGet(orderRepository::findAll);
        return orderMapper.toResponseLightDtoList(orders);
    }

    // READ ONE
    public OrderResponseDTO read(String id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Keine Bestellung mit der ID " + id + " vorhanden"));
        return orderMapper.toResponseDto(order);
    }

    // CREATE
    public OrderResponseDTO create(OrderCreateDTO dto) {
        // optional: Validierung (z. B. total >= 0) ist schon per Bean Validation im DTO
        User createdBy = null;
        if (dto.getCreatedById() != null && !dto.getCreatedById().isBlank()) {
            createdBy = userRepository.findById(dto.getCreatedById())
                    .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User (createdById) nicht gefunden"));
        }
        Order entity = orderMapper.toEntity(dto, createdBy);
        Order saved = orderRepository.save(entity);
        return orderMapper.toResponseDto(saved);
    }

    // UPDATE (optional; falls du Update erlauben willst)
    public OrderResponseDTO update(String id, OrderUpdateDTO dto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Keine Bestellung mit der ID " + id + " vorhanden"));
        orderMapper.updateEntity(dto, order);
        Order saved = orderRepository.save(order);
        return orderMapper.toResponseDto(saved);
    }
}
