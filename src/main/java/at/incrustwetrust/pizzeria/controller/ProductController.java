package at.incrustwetrust.pizzeria.controller;

import at.incrustwetrust.pizzeria.dto.order.OrderResponseLightDTO;
import at.incrustwetrust.pizzeria.dto.product.ProductResponseDTO;
import at.incrustwetrust.pizzeria.dto.product.ProductResponseLightDTO;
import at.incrustwetrust.pizzeria.entity.Order;
import at.incrustwetrust.pizzeria.entity.Product;
import at.incrustwetrust.pizzeria.mapper.OrderMapper;
import at.incrustwetrust.pizzeria.mapper.ProductMapper;
import at.incrustwetrust.pizzeria.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping ("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService){ this.productService = productService;}

    @GetMapping
    public List<ProductResponseLightDTO> readAll() {
        List<Product> products = this.productService.readAll();
        List<ProductResponseLightDTO> productsResponseLight;
        productsResponseLight = products.stream()
                .map(ProductMapper::toResponseLightDto)
                .toList();
        return productsResponseLight;
    }


    @GetMapping("/{id}")
    public ProductResponseDTO read(@PathVariable String id) {
          return ProductMapper.toResponseDto(this.productService.read(id));
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