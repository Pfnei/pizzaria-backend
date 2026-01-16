package at.incrustwetrust.pizzeria.controller;

import at.incrustwetrust.pizzeria.dto.order.*;
import at.incrustwetrust.pizzeria.security.SecurityUser;
import at.incrustwetrust.pizzeria.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> readAll(
            @RequestParam(required = false) String createdBy, @AuthenticationPrincipal SecurityUser principal) {
        return ResponseEntity.ok(orderService.readAll(Optional.ofNullable(createdBy), principal));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> read(@PathVariable String id,@AuthenticationPrincipal SecurityUser principal) {
        return ResponseEntity.ok(orderService.read(id,principal));
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> create(@RequestBody @Valid OrderCreateDTO dto,@AuthenticationPrincipal SecurityUser principal) {
        OrderResponseDTO created = orderService.create(dto,principal);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Optional, falls Updates erlaubt sind:
    @PreAuthorize("hasRole('ADMIN') || principal.id == #userId")
    @PatchMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> update(
            @PathVariable String id,
            @RequestBody @Valid OrderUpdateDTO dto,@AuthenticationPrincipal SecurityUser principal) {
        return ResponseEntity.ok(orderService.update(id, dto,principal));
    }
}
