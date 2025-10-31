package at.incrustwetrust.pizzeria.controller;


import at.incrustwetrust.pizzeria.dto.order.OrderResponseDTO;
import at.incrustwetrust.pizzeria.entity.Order;
import at.incrustwetrust.pizzeria.service.OrderService;
import at.incrustwetrust.pizzeria.mapper.OrderMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;


@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService){ this.orderService = orderService;}


    @GetMapping
    public List<OrderResponseDTO> readAll(@RequestParam(required = false) String createdBy) {
        List<Order> orders;
        if (createdBy == null) {
            orders =  orderService.readAll(Optional.empty());
        }
        else {
           orders =  orderService.readAll(Optional.of(createdBy));
        }
        List<OrderResponseDTO> ordersResponse;
        ordersResponse = orders.stream()
                .map(OrderMapper::toResponseDto)
                .toList();
        return ordersResponse;
    }

    @GetMapping ("/{id}")
    public OrderResponseDTO read(@PathVariable String id) {
        return OrderMapper.toResponseDto(this.orderService.read(id));
    }

    @PostMapping
    @ResponseStatus (HttpStatus.CREATED)
    public Order create (@RequestBody @Valid Order order) {return this.orderService.create(order);}

    // no Order update possible
    // no Order deletion possible


}
