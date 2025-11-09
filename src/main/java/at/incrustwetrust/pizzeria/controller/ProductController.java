package at.incrustwetrust.pizzeria.controller;

import at.incrustwetrust.pizzeria.dto.product.ProductCreateDTO;
import at.incrustwetrust.pizzeria.dto.product.ProductUpdateDTO;
import at.incrustwetrust.pizzeria.dto.product.ProductResponseDTO;
import at.incrustwetrust.pizzeria.dto.product.ProductResponseLightDTO;
import at.incrustwetrust.pizzeria.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // =====================================================
    // READ ALL (Light DTO)
    // =====================================================
    @GetMapping
    public ResponseEntity<List<ProductResponseLightDTO>> readAll() {
        List<ProductResponseLightDTO> products = productService.readAll();
        return ResponseEntity.ok(products);
    }

    // =====================================================
    // READ ONE (Full DTO)
    // =====================================================
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> read(@PathVariable String id) {
        ProductResponseDTO product = productService.read(id);
        return ResponseEntity.ok(product);
    }

    // =====================================================
    // CREATE
    // =====================================================
    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(@RequestBody @Valid ProductCreateDTO dto) {
        ProductResponseDTO created = productService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // =====================================================
    // UPDATE (PATCH or PUT)
    // =====================================================
    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> update(
            @PathVariable String id,
            @RequestBody @Valid ProductUpdateDTO dto) {

        ProductResponseDTO updated = productService.update(dto, id);
        return ResponseEntity.ok(updated);
    }

    // =====================================================
    // DELETE
    // =====================================================
    @DeleteMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> delete(@PathVariable String id) {
        ProductResponseDTO deleted = productService.delete(id);
        return ResponseEntity.ok(deleted);
    }
}
