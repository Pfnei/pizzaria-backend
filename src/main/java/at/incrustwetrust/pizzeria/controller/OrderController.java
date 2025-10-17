package at.incrustwetrust.pizzeria.controller;


import at.incrustwetrust.pizzeria.entity.Order;
import at.incrustwetrust.pizzeria.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService){ this.orderService = orderService;}


    @GetMapping
    public List<Order> readAll() {
        return this.orderService.readAll();
    }

    @GetMapping ("/{id}")
    public Order read(@PathVariable String id) {return this.orderService.read(id);}

    @PostMapping
    @ResponseStatus (HttpStatus.CREATED)
    public Order create (@RequestBody @Valid Order order) {return this.orderService.create(order);}

    // no Order update possible
    // no Order deletion possible


}
