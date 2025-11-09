package at.incrustwetrust.pizzeria.service;

import at.incrustwetrust.pizzeria.dto.order.*;
import at.incrustwetrust.pizzeria.entity.Order;
import at.incrustwetrust.pizzeria.mapper.OrderMapper;
import at.incrustwetrust.pizzeria.mapper.UserMapper;
import at.incrustwetrust.pizzeria.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final UserMapper userMapper;

    // =====================================================
    // READ ALL (optional filter by createdBy)
    // =====================================================
    public List<OrderResponseLightDTO> readAll(Optional<String> createdBy) {
        List<Order> orders = createdBy
                .map(orderRepository::findAllByCreatedBy_UserId)
                .orElseGet(orderRepository::findAll);
        return orderMapper.toResponseLightDtoList(orders, userMapper);
    }

    // =====================================================
    // READ ONE
    // =====================================================
    public OrderResponseDTO read(String id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Keine Bestellung mit der ID " + id + " vorhanden"));
        return orderMapper.toResponseDto(order, userMapper);
    }

    // =====================================================
    // CREATE
    // =====================================================
    public OrderResponseDTO create(OrderCreateDTO dto) {
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ungültige Bestelldaten");
        }

        Order order = orderMapper.toEntity(dto);
        Order saved = orderRepository.save(order);
        return orderMapper.toResponseDto(saved, userMapper);
    }

    // =====================================================
    // UPDATE (PATCH)
    // =====================================================
    public OrderResponseDTO update(OrderUpdateDTO dto, String id) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Keine Bestellung mit der ID " + id + " vorhanden"));

        // MapStruct patched nur Felder, die im DTO nicht null sind
        orderMapper.updateEntity(dto, existingOrder);

        Order saved = orderRepository.save(existingOrder);
        return orderMapper.toResponseDto(saved, userMapper);
    }
}
