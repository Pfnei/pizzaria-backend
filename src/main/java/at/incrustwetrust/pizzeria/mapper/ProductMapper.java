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

            // Diese beiden Ziele existieren in der Entity, sind aber hier (erstmal) nicht befüllt:
            @Mapping(target = "productPicture", ignore = true),
            @Mapping(target = "orders", ignore = true),

            // Allergene brauchen Lookup -> erstmal ignorieren
            @Mapping(target = "allergens", ignore = true)
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
            @Mapping(target = "productPicture", ignore = true),
            @Mapping(target = "orders", ignore = true),

            // Gleiches Thema: kein automatisches Mapping ohne Lookup
            @Mapping(target = "allergens", ignore = true)
    })
    void updateEntity(ProductUpdateDTO dto, @MappingTarget Product entity);

    // ============== ENTITY -> DETAIL DTO ==============
    @Mappings({
            // boolean Felder mit anderem Namen im DTO
            @Mapping(target = "vegetarian", source = "vegetarian"),
            @Mapping(target = "active", source = "active"),

            // verschachtelte User-Felder (nutzt UserMapper automatisch)
            @Mapping(target = "createdById", source = "createdBy.userId"),
            @Mapping(target = "createdBy",   source = "createdBy"),
            @Mapping(target = "lastUpdatedById", source = "lastUpdatedBy.userId"),
            @Mapping(target = "lastUpdatedBy",   source = "lastUpdatedBy"),

            // Allergene als Liste von Abkürzungen (String)
            @Mapping(target = "allergens", source = "allergens", qualifiedByName = "allergensToStrings")
    })
    ProductResponseDTO toResponseDto(Product p);

    // ============== ENTITY -> LIGHT DTO ==============
    @Mappings({
            // ACHTUNG: In ProductResponseLightDTO heißen die Felder 'vegetarian' und 'active'
            @Mapping(target = "vegetarian", source = "vegetarian"),
            @Mapping(target = "active",     source = "active"),
            @Mapping(target = "allergens",  source = "allergens", qualifiedByName = "allergensToStrings")
    })
    ProductResponseLightDTO toResponseLightDto(Product p);

    // ============== LISTEN ==============
    List<ProductResponseDTO> toResponseDtoList(List<Product> products);
    List<ProductResponseLightDTO> toResponseLightDtoList(List<Product> products);

    // ============== HELFER: Allergene -> Strings ==============
    @Named("allergensToStrings")
    default List<String> allergensToStrings(List<Allergen> allergens) {
        if (allergens == null) return null;
        return allergens.stream()
                .map(Allergen::getAbbreviation) // <- Abkürzung (String) aus deiner Entity
                .collect(Collectors.toList());
    }
}
