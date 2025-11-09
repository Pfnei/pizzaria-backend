package at.incrustwetrust.pizzeria.service;

import at.incrustwetrust.pizzeria.dto.product.*;
import at.incrustwetrust.pizzeria.entity.Product;
import at.incrustwetrust.pizzeria.mapper.ProductMapper;
import at.incrustwetrust.pizzeria.mapper.UserMapper;
import at.incrustwetrust.pizzeria.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final UserMapper userMapper;

    // =====================================================
    // CREATE
    // =====================================================
    public ProductResponseDTO create(ProductCreateDTO dto) {
        ifProductNameAlreadyExistsThrow(dto.getProductName());

        Product product = productMapper.toEntity(dto);
        Product saved = productRepository.save(product);

        return productMapper.toResponseDto(saved, userMapper);
    }

    // =====================================================
    // READ
    // =====================================================
    public ProductResponseDTO read(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(NOT_FOUND, "Kein Produkt mit der ID " + id + " vorhanden"));

        return productMapper.toResponseDto(product, userMapper);
    }

    public List<ProductResponseLightDTO> readAll() {
        List<Product> products = productRepository.findAll();
        return productMapper.toResponseLightDtoList(products);
    }

    // =====================================================
    // UPDATE
    // =====================================================
    public ProductResponseDTO update(ProductUpdateDTO dto, String id) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(NOT_FOUND, "Produkt-ID nicht in der Datenbank"));

        ifProductNameAlreadyExistsThrow(dto.getProductName(), id);

        productMapper.updateEntity(dto, existing);

        Product saved = productRepository.save(existing);
        return productMapper.toResponseDto(saved, userMapper);
    }

    // =====================================================
    // DELETE
    // =====================================================
    public ProductResponseDTO delete(String id) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(NOT_FOUND, "Kein Produkt mit der ID " + id + " vorhanden"));

        productRepository.delete(existing);
        return productMapper.toResponseDto(existing, userMapper);
    }

    // =====================================================
    // DUPLICATE CHECKS
    // =====================================================
    private void ifProductNameAlreadyExistsThrow(String productName) {
        productRepository.findProductByProductName(productName).ifPresent(p -> {
            throw new ResponseStatusException(CONFLICT, "Es ist bereits ein Produkt mit diesem Namen vorhanden");
        });
    }

    private void ifProductNameAlreadyExistsThrow(String productName, String excludedId) {
        productRepository.findProductByProductNameAndProductIdNot(productName, excludedId).ifPresent(p -> {
            throw new ResponseStatusException(CONFLICT, "Es ist bereits ein Produkt mit diesem Namen vorhanden");
        });
    }
}
