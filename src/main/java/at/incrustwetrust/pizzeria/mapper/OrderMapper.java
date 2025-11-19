package at.incrustwetrust.pizzeria.mapper;

import at.incrustwetrust.pizzeria.dto.order.OrderResponseDTO;
import at.incrustwetrust.pizzeria.dto.order.OrderResponseLightDTO;
import at.incrustwetrust.pizzeria.dto.user.UserResponseLightDTO;
import at.incrustwetrust.pizzeria.entity.Order;

import org.springframework.stereotype.Component;

@Component
public class OrderMapper {


    public static OrderResponseDTO toResponseDto(Order order) {
        if (order == null) return null;

        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setOrderId(order.getOrderId());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setDeliveredAt(order.getDeliveredAt());
        dto.setTotal(order.getTotal());

        dto.setFirstname(order.getFirstname());
        dto.setLastname(order.getLastname());
        dto.setPhoneNumber(order.getPhoneNumber());
        dto.setAddress(order.getAddress());
        dto.setZipcode(order.getZipcode());
        dto.setCity(order.getCity());
        dto.setDeliveryNote(order.getDeliveryNote());

        dto.setCreatedById(order.getCreatedBy() != null ? order.getCreatedBy().getUserId() : null);
        dto.setCreatedBy(order.getCreatedBy() != null ? UserMapper.toResponseLightDto(order.getCreatedBy()) : null);
        dto.setItems(order.getItems() != null ? order.getItems() : null);

        return dto;
    }

    public static OrderResponseLightDTO toResponseLightDto(Order order) {
        if (order == null) return null;

        OrderResponseLightDTO dto = new OrderResponseLightDTO();
        dto.setOrderId(order.getOrderId());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setDeliveredAt(order.getDeliveredAt());
        dto.setTotal(order.getTotal());

        dto.setFirstname(order.getFirstname());
        dto.setLastname(order.getLastname());
        dto.setPhoneNumber(order.getPhoneNumber());
        dto.setAddress(order.getAddress());
        dto.setZipcode(order.getZipcode());
        dto.setCity(order.getCity());
        dto.setDeliveryNote(order.getDeliveryNote());

        dto.setCreatedById(order.getCreatedBy() != null ? order.getCreatedBy().getUserId() : null);
        dto.setCreatedBy((order.getCreatedBy() != null) ? UserMapper.toResponseLightDto(order.getCreatedBy()) : null);

        return dto;
    }
}
