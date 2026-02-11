package at.incrustwetrust.pizzeria.controller;

import at.incrustwetrust.pizzeria.dto.product.*;
import at.incrustwetrust.pizzeria.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<List<ProductResponseLightDTO>> readAll() {
        List<ProductResponseLightDTO> products = productService.readAll();
        return ResponseEntity.ok(products);
    }


    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<ProductResponseDTO> read(@PathVariable String id) {
        ProductResponseDTO product = productService.read(id);
        return ResponseEntity.ok(product);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(@RequestBody @Valid ProductCreateDTO dto) {
        ProductResponseDTO created = productService.create(dto);
        return ResponseEntity.ok(created);
    }


    @PreAuthorize( "hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> update(
            @PathVariable String id,
            @RequestBody @Valid ProductUpdateDTO dto) {

        ProductResponseDTO updated = productService.update(dto, id);
        return ResponseEntity.ok(updated);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDTO> delete(@PathVariable String id) {
        return ResponseEntity.ok(productService.delete(id));
    }
}
