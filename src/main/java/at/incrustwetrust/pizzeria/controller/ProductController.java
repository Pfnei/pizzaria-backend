package at.incrustwetrust.pizzeria.controller;

import at.incrustwetrust.pizzeria.entity.Product;
import at.incrustwetrust.pizzeria.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping ("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService){ this.productService = productService;}

    @GetMapping
    public List<Product> readAll() {
        return this.productService.readAll();
    }

    @GetMapping("/{id}")
    public Product read(@PathVariable String id) {
        return this.productService.read(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(@RequestBody @Valid Product product) {
        return this.productService.create(product);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Product update(@RequestBody @Valid Product product, @PathVariable String id) {
        return this.productService.update(product, id);
    }

    // to be discussed - if it should be possible
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Product delete(@PathVariable String id) {
        return this.productService.delete(id);
    }
}