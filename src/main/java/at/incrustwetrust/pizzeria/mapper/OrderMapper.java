package at.incrustwetrust.pizzeria.mapper;

import at.incrustwetrust.pizzeria.dto.order.*;
import at.incrustwetrust.pizzeria.entity.Order;
import at.incrustwetrust.pizzeria.entity.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = { UserMapper.class, ProductMapper.class })
public interface OrderMapper {

    // ======= CREATE DTO -> ENTITY =======
    @Mappings({
            @Mapping(target = "orderId", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "deliveredAt", ignore = true),
            @Mapping(target = "items", ignore = true),
            @Mapping(target = "createdBy", ignore = true), // will be set in AfterMapping
            // HERE THE FIXES: Explicitly specify the DTO as source
            @Mapping(source = "dto.firstname", target = "firstname"),
            @Mapping(source = "dto.lastname", target = "lastname"),
            @Mapping(source = "dto.phoneNumber", target = "phoneNumber"),
            @Mapping(source = "dto.address", target = "address"),
            @Mapping(source = "dto.zipcode", target = "zipcode"),
            @Mapping(source = "dto.city", target = "city")
    })
    Order toEntity(OrderCreateDTO dto, User createdBy);

    // ======= UPDATE DTO -> ENTITY (PATCH) =======
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "orderId", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "items", ignore = true)
            // deliveredAt, total, address, ... will be taken over if set
            // status does not exist in the Entity -> will be automatically ignored (source property without target)
    })
    void updateEntity(OrderUpdateDTO dto, @MappingTarget Order order);

    // ======= ENTITY -> RESPONSE (DETAIL) =======
    @Mappings({
            @Mapping(target = "createdById",
                    expression = "java(o.getCreatedBy()!=null ? o.getCreatedBy().getUserId() : null)"),
            @Mapping(target = "createdBy", source = "createdBy") // uses UserMapper.toResponseLightDto
    })
    OrderResponseDTO toResponseDto(Order o);

    // ======= ENTITY -> RESPONSE (LIGHT) =======
    @Mappings({
            @Mapping(target = "createdById",
                    expression = "java(o.getCreatedBy()!=null ? o.getCreatedBy().getUserId() : null)"),
            @Mapping(target = "createdBy", source = "createdBy")
    })
    OrderResponseLightDTO toResponseLightDto(Order o);

    OrderItemResponseDTO toOrderItemResponseDto(at.incrustwetrust.pizzeria.entity.OrderItem item);

    List<OrderResponseDTO> toResponseDtoList(List<Order> orders);
    List<OrderResponseLightDTO> toResponseLightDtoList(List<Order> orders);

    // ======= AfterMapping: set createdBy from @Context =======
    @AfterMapping
    default void setCreatedBy(@MappingTarget Order order, @Context User createdBy) {
        if (createdBy != null) {
            order.setCreatedBy(createdBy);
        }
    }
}
