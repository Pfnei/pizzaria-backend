package at.incrustwetrust.pizzeria.mapper;

import at.incrustwetrust.pizzeria.dto.product.ProductResponseDTO;
import at.incrustwetrust.pizzeria.dto.product.ProductResponseLightDTO;
import at.incrustwetrust.pizzeria.entity.Product;

import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public static ProductResponseDTO toResponseDto(Product product) {
        if (product == null) return null;

        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setProductId(product.getProductId());
        dto.setProductName(product.getProductName());
        dto.setProductDescription(product.getProductDescription());
        dto.setPrice(product.getPrice());

        dto.setIsVegetarian(product.isVegetarian());
        dto.setIsActive(product.isActive());

        dto.setMainCategory(product.getMainCategory());
        dto.setSubCategory(product.getSubCategory());

        dto.setAllergens(product.getAllergens() != null ? product.getAllergens() : null);

        dto.setCreatedAt(product.getCreatedAt());
        dto.setCreatedById(product.getCreatedBy() != null ? product.getCreatedBy().getUserId() : null);
        dto.setCreatedBy(product.getCreatedBy() != null ? UserMapper.toResponseLightDto(product.getCreatedBy()) : null);

        dto.setLastUpdatedAt(product.getLastUpdatedAt());
        dto.setLastUpdatedById(product.getLastUpdatedBy() != null ? product.getLastUpdatedBy().getUserId() : null);
        dto.setLastUpdatedBy(product.getLastUpdatedBy() != null ? UserMapper.toResponseLightDto(product.getLastUpdatedBy()) : null);

        return dto;
    }

    public static ProductResponseLightDTO toResponseLightDto(Product product) {
        if (product == null) return null;

        ProductResponseLightDTO dto = new ProductResponseLightDTO();
        dto.setProductId(product.getProductId());
        dto.setProductName(product.getProductName());
        dto.setPrice(product.getPrice());

        dto.setIsVegetarian(product.isVegetarian());
        dto.setIsActive(product.isActive());

        dto.setMainCategory(product.getMainCategory());
        dto.setSubCategory(product.getSubCategory());

        dto.setAllergens(product.getAllergens() != null ? product.getAllergens() : null);


        return dto;
    }
}
