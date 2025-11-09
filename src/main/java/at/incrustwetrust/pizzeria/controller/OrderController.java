package at.incrustwetrust.pizzeria.controller;

import at.incrustwetrust.pizzeria.dto.order.*;
import at.incrustwetrust.pizzeria.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderResponseLightDTO>> readAll(
            @RequestParam(required = false) String createdBy) {
        return ResponseEntity.ok(orderService.readAll(Optional.ofNullable(createdBy)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> read(@PathVariable String id) {
        return ResponseEntity.ok(orderService.read(id));
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> create(@RequestBody @Valid OrderCreateDTO dto) {
        OrderResponseDTO created = orderService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Optional, falls Updates erlaubt sind:
    @PatchMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> update(
            @PathVariable String id,
            @RequestBody @Valid OrderUpdateDTO dto) {
        return ResponseEntity.ok(orderService.update(id, dto));
    }
}
