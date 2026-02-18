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
    
    
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mine")
    public ResponseEntity<List<OrderResponseDTO>> getMyOrders( @AuthenticationPrincipal SecurityUser principal) {
        return ResponseEntity.ok(orderService.readMyOrders(principal));
    }
    
    
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> readAll(
            @RequestParam(required = false) String createdBy,
            @RequestParam(required = false) String productId,
            @AuthenticationPrincipal SecurityUser principal) {
        return ResponseEntity.ok(orderService.readAll(Optional.ofNullable(createdBy), Optional.ofNullable(productId), principal));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> read(@PathVariable String id,@AuthenticationPrincipal SecurityUser principal) {
        return ResponseEntity.ok(orderService.read(id,principal));
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> create(@RequestBody @Valid OrderCreateDTO dto,@AuthenticationPrincipal SecurityUser principal) {

        return ResponseEntity.ok(orderService.create(dto,principal));

    }

    // Optional, if updates are allowed:
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> update(
            @PathVariable String id,
            @RequestBody @Valid OrderUpdateDTO dto,@AuthenticationPrincipal SecurityUser principal) {

        return ResponseEntity.ok(orderService.update(id, dto,principal));
    }
}
