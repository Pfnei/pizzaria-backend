package at.incrustwetrust.pizzeria.mapper;

import at.incrustwetrust.pizzeria.dto.product.*;
import at.incrustwetrust.pizzeria.entity.Product;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface ProductMapper {

    // ==========================================================
    // DTO → ENTITY
    // ==========================================================
    @Mappings({
            @Mapping(target = "productId", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "lastUpdatedAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "lastUpdatedBy", ignore = true)
    })
    Product toEntity(ProductCreateDTO dto);

    // ==========================================================
    // UPDATE EXISTING ENTITY (PATCH-STYLE)
    // ==========================================================
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "productId", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "lastUpdatedAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "lastUpdatedBy", ignore = true)
    })
    void updateEntity(ProductUpdateDTO dto, @MappingTarget Product entity);

    // ==========================================================
    // ENTITY → DETAIL DTO
    // ==========================================================
    @Mappings({
            @Mapping(target = "isVegetarian", source = "vegetarian"),
            @Mapping(target = "isActive", source = "active"),

            // nested mappings for created/updated users
            @Mapping(target = "createdById",
                    expression = "java(p.getCreatedBy() != null ? p.getCreatedBy().getUserId() : null)"),
            @Mapping(target = "lastUpdatedById",
                    expression = "java(p.getLastUpdatedBy() != null ? p.getLastUpdatedBy().getUserId() : null)"),

            @Mapping(target = "createdBy",
                    expression = "java(p.getCreatedBy() != null ? userMapper.toResponseLightDto(p.getCreatedBy()) : null)"),
            @Mapping(target = "lastUpdatedBy",
                    expression = "java(p.getLastUpdatedBy() != null ? userMapper.toResponseLightDto(p.getLastUpdatedBy()) : null)")
    })
    ProductResponseDTO toResponseDto(Product p, @Context UserMapper userMapper);

    // ==========================================================
    // ENTITY → LIGHT DTO
    // ==========================================================
    @Mappings({
            @Mapping(target = "isVegetarian", source = "vegetarian"),
            @Mapping(target = "isActive", source = "active")
    })
    ProductResponseLightDTO toResponseLightDto(Product p);

    // ==========================================================
    // LISTEN
    // ==========================================================
    List<ProductResponseDTO> toResponseDtoList(List<Product> products, @Context UserMapper userMapper);
    List<ProductResponseLightDTO> toResponseLightDtoList(List<Product> products);
}
