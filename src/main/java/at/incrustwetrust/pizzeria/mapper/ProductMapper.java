package at.incrustwetrust.pizzeria.mapper;

import at.incrustwetrust.pizzeria.dto.product.*;
import at.incrustwetrust.pizzeria.entity.Allergen;
import at.incrustwetrust.pizzeria.entity.Product;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface ProductMapper {

    // ============== DTO -> ENTITY (CREATE) ==============
    @Mappings({
            @Mapping(target = "productId", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "lastUpdatedAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "lastUpdatedBy", ignore = true),
            @Mapping(target = "orders", ignore = true),
            @Mapping(target = "allergens", ignore = true) // Will be set in Service via Repository-Lookup
    })
    Product toEntity(ProductCreateDTO dto);

    // ============== DTO -> ENTITY (UPDATE / PATCH) ==============
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "productId", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "lastUpdatedAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "lastUpdatedBy", ignore = true),
            @Mapping(target = "orders", ignore = true),
            @Mapping(target = "allergens", ignore = true) // Will be handled manually in Service
    })
    void updateEntity(ProductUpdateDTO dto, @MappingTarget Product entity);

    // ============== ENTITY -> DETAIL DTO ==============
    @Mappings({
            // Extract IDs
            @Mapping(target = "createdById", source = "createdBy.userId"),
            @Mapping(target = "lastUpdatedById", source = "lastUpdatedBy.userId"),
            // Convert allergens
            @Mapping(target = "allergens", source = "allergens", qualifiedByName = "allergensToStrings")
    })
    ProductResponseDTO toResponseDto(Product p);

    // ============== ENTITY -> LIGHT DTO ==============
    @Mappings({
            @Mapping(target = "allergens", source = "allergens", qualifiedByName = "allergensToStrings")
    })
    ProductResponseLightDTO toResponseLightDto(Product p);

    // ============== LISTS ==============
    List<ProductResponseDTO> toResponseDtoList(List<Product> products);
    List<ProductResponseLightDTO> toResponseLightDtoList(List<Product> products);

    // ============== HELPER: Allergens -> Strings ==============
    @Named("allergensToStrings")
    default List<String> allergensToStrings(List<Allergen> allergens) {
        if (allergens == null) return null;
        return allergens.stream()
                .map(Allergen::getAbbreviation)
                .collect(Collectors.toList());
    }
}
