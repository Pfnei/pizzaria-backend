package at.incrustwetrust.pizzeria.service;

import at.incrustwetrust.pizzeria.dto.product.ProductCreateDTO;
import at.incrustwetrust.pizzeria.dto.product.ProductResponseDTO;
import at.incrustwetrust.pizzeria.dto.product.ProductResponseLightDTO;
import at.incrustwetrust.pizzeria.dto.product.ProductUpdateDTO;
import at.incrustwetrust.pizzeria.entity.Allergen;
import at.incrustwetrust.pizzeria.entity.Product;
import at.incrustwetrust.pizzeria.entity.User;
import at.incrustwetrust.pizzeria.exception.ProductAlreadyExistsException;
import at.incrustwetrust.pizzeria.exception.ResourceNotFoundException;
import at.incrustwetrust.pizzeria.mapper.ProductMapper;
import at.incrustwetrust.pizzeria.repository.AllergenRepository;
import at.incrustwetrust.pizzeria.repository.ProductRepository;
import at.incrustwetrust.pizzeria.utils.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;



@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final AllergenRepository allergenRepository;
    private final CurrentUserService currentUserService;
    private final ProductMapper productMapper;


    // CREATE
    
    public ProductResponseDTO create(ProductCreateDTO dto) {
        ifProductNameAlreadyExistsThrow(dto.getProductName());
        
        Product product = productMapper.toEntity(dto);
        
        User currentUser = currentUserService.getCurrentUserEntity();
        product.setCreatedBy(currentUser);
        product.setLastUpdatedBy(currentUser);
        
        if (dto.getAllergens() != null) {
            List<Allergen> allergens = allergenRepository.findAllById(dto.getAllergens());
            product.setAllergens(allergens);
        }
        
        Product saved = productRepository.save(product);
        return productMapper.toResponseDto(saved);
    }

    // READ

    public ProductResponseDTO read(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Kein Produkt mit der ID " + id + " vorhanden"));

        return productMapper.toResponseDto(product);
    }


    public List<ProductResponseLightDTO> readAll() {
        List<Product> products = productRepository.findAll();
        return productMapper.toResponseLightDtoList(products);
    }


    // UPDATE
    
    public ProductResponseDTO update(ProductUpdateDTO dto, String id) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produkt-ID nicht in der Datenbank"));
        
        ifProductNameAlreadyExistsThrow(dto.getProductName(), id);
        
        productMapper.updateEntity(dto, existing);
        
        User currentUser = currentUserService.getCurrentUserEntity();
        existing.setLastUpdatedBy(currentUser);
        
        if (dto.getAllergens() != null) {
            List<Allergen> allergens = allergenRepository.findAllById(dto.getAllergens());
            
            if (existing.getAllergens() != null) {
                existing.getAllergens().clear();
                existing.getAllergens().addAll(allergens);
            } else {
                existing.setAllergens(allergens);
            }
        }
        
        Product saved = productRepository.save(existing);
        return productMapper.toResponseDto(saved);
    }

    // DELETE

    public ProductResponseDTO delete(String id) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Kein Produkt mit der ID " + id + " vorhanden"));

        productRepository.delete(existing);
        return productMapper.toResponseDto(existing);
    }


    // DUPLICATE CHECKS

    private void ifProductNameAlreadyExistsThrow(String productName) {
        productRepository.findProductByProductName(productName).ifPresent(p -> {
            throw new ProductAlreadyExistsException("Es ist bereits ein Produkt mit diesem Namen vorhanden");
        });
    }

    private void ifProductNameAlreadyExistsThrow(String productName, String excludedId) {
        productRepository.findProductByProductNameAndProductIdNot(productName, excludedId).ifPresent(p -> {
            throw new ProductAlreadyExistsException("Es ist bereits ein Produkt mit diesem Namen vorhanden");
        });
    }
}
