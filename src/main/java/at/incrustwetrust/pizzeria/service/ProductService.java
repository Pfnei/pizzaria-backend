package at.incrustwetrust.pizzeria.service;

import at.incrustwetrust.pizzeria.dto.product.*;
import at.incrustwetrust.pizzeria.entity.Allergen;
import at.incrustwetrust.pizzeria.entity.Order;
import at.incrustwetrust.pizzeria.entity.Product;
import at.incrustwetrust.pizzeria.entity.User;
import at.incrustwetrust.pizzeria.exception.ProductAlreadyExistsException;
import at.incrustwetrust.pizzeria.exception.ResourceNotFoundException;
import at.incrustwetrust.pizzeria.exception.UnauthorizedActionException;
import at.incrustwetrust.pizzeria.mapper.ProductMapper;
import at.incrustwetrust.pizzeria.repository.AllergenRepository;
import at.incrustwetrust.pizzeria.repository.ProductRepository;
import at.incrustwetrust.pizzeria.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final AllergenRepository allergenRepository;
    private final CurrentUserService currentUserService;
    private final ProductMapper productMapper;
    private final FileStorageService fileService;


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
                        new ResourceNotFoundException("No product found with ID " + id));

        return productMapper.toResponseDto(product);
    }


    public List<ProductResponseLightDTO> readAll(Optional<String> createdBy, SecurityUser principal) {
        loggedInUserCheck(principal);
        
        List<Product> products = null;
        
        if (principal.isAdmin()) if (createdBy.isPresent()) {
			products = productRepository.findAllByCreatedBy_UserId(createdBy.get());
		} else {
			products = productRepository.findAll();
		}
        return productMapper.toResponseLightDtoList(products);
    }

    // UPDATE
    
    public ProductResponseDTO update(ProductUpdateDTO dto, String id) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product ID not in database"));
        
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
                        new ResourceNotFoundException("No product found with ID " + id));

        // Delete image if exists
        if (existing.getProductPicture() != null) {
            fileService.deleteProductImage(existing.getProductPicture());
        }

        // Map first, then delete
        ProductResponseDTO response = productMapper.toResponseDto(existing);

        productRepository.delete(existing);
        return response;
    }


    // DUPLICATE CHECKS

    private void ifProductNameAlreadyExistsThrow(String productName) {
        productRepository.findProductByProductName(productName).ifPresent(p -> {
            throw new ProductAlreadyExistsException("A product with this name already exists");
        });
    }

    private void ifProductNameAlreadyExistsThrow(String productName, String excludedId) {
        productRepository.findProductByProductNameAndProductIdNot(productName, excludedId).ifPresent(p -> {
            throw new ProductAlreadyExistsException("A product with this name already exists");
        });
    }
    
    private void loggedInUserCheck(SecurityUser principal) {
        if (principal == null) {
            throw new UnauthorizedActionException("your not logged in");
        }
    }
    
}
