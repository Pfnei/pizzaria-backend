package at.incrustwetrust.pizzeria.mapper;

import at.incrustwetrust.pizzeria.dto.order.OrderCreateDTO;
import at.incrustwetrust.pizzeria.dto.order.OrderUpdateDTO;
import at.incrustwetrust.pizzeria.dto.order.OrderResponseDTO;
import at.incrustwetrust.pizzeria.dto.order.OrderResponseLightDTO;
import at.incrustwetrust.pizzeria.entity.Order;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface OrderMapper {

    // =====================================================
    // DTO → ENTITY (CREATE)
    // =====================================================
    @Mappings({
            @Mapping(target = "orderId", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "deliveredAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "items", ignore = true),
            @Mapping(target = "lastUpdatedBy", ignore = true)
    })
    Order toEntity(OrderCreateDTO dto);

    // =====================================================
    // PATCH/UPDATE (optional für Admins)
    // =====================================================
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "orderId", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "items", ignore = true),
            @Mapping(target = "lastUpdatedBy", ignore = true)
    })
    void updateEntity(OrderUpdateDTO dto, @MappingTarget Order order);

    // =====================================================
    // ENTITY → FULL DTO
    // =====================================================
    @Mappings({
            @Mapping(target = "createdById",
                    expression = "java(o.getCreatedBy() != null ? o.getCreatedBy().getUserId() : null)"),
            @Mapping(target = "createdBy",
                    expression = "java(o.getCreatedBy() != null ? userMapper.toResponseLightDto(o.getCreatedBy()) : null)")
    })
    OrderResponseDTO toResponseDto(Order o, @Context UserMapper userMapper);

    // =====================================================
    // ENTITY → LIGHT DTO
    // =====================================================
    @Mappings({
            @Mapping(target = "createdById",
                    expression = "java(o.getCreatedBy() != null ? o.getCreatedBy().getUserId() : null)"),
            @Mapping(target = "createdBy",
                    expression = "java(o.getCreatedBy() != null ? userMapper.toResponseLightDto(o.getCreatedBy()) : null)")
    })
    OrderResponseLightDTO toResponseLightDto(Order o, @Context UserMapper userMapper);

    // =====================================================
    // LIST MAPPINGS
    // =====================================================
    List<OrderResponseDTO> toResponseDtoList(List<Order> orders, @Context UserMapper userMapper);
    List<OrderResponseLightDTO> toResponseLightDtoList(List<Order> orders, @Context UserMapper userMapper);
}
