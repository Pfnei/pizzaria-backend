package at.incrustwetrust.pizzeria.controller;

import at.incrustwetrust.pizzeria.dto.order.OrderCreateDTO;
import at.incrustwetrust.pizzeria.dto.order.OrderResponseDTO;
import at.incrustwetrust.pizzeria.dto.order.OrderResponseLightDTO;
import at.incrustwetrust.pizzeria.dto.order.OrderUpdateDTO;
import at.incrustwetrust.pizzeria.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // =====================================================
    // READ ALL (optional filter by createdBy)
    // =====================================================
    @GetMapping
    public ResponseEntity<List<OrderResponseLightDTO>> readAll(
            @RequestParam(required = false) String createdBy) {

        List<OrderResponseLightDTO> orders = orderService.readAll(Optional.ofNullable(createdBy));
        return ResponseEntity.ok(orders);
    }

    // =====================================================
    // READ ONE (FULL DTO)
    // =====================================================
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> read(@PathVariable String id) {
        return ResponseEntity.ok(orderService.read(id));
    }

    // =====================================================
    // CREATE (über DTO)
    // =====================================================
    @PostMapping
    public ResponseEntity<OrderResponseDTO> create(@Valid @RequestBody OrderCreateDTO dto) {
        OrderResponseDTO created = orderService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // =====================================================
    // UPDATE (PATCH)
    // =====================================================
    @PatchMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> update(
            @PathVariable String id,
            @Valid @RequestBody OrderUpdateDTO dto) {

        OrderResponseDTO updated = orderService.update(dto, id);
        return ResponseEntity.ok(updated);
    }
}
