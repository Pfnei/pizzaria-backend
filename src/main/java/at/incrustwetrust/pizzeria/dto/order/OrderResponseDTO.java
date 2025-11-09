package at.incrustwetrust.pizzeria.dto.order;

import at.incrustwetrust.pizzeria.dto.user.UserResponseLightDTO;
import at.incrustwetrust.pizzeria.entity.OrderItem;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrderResponseDTO {

    private String orderId;
    private Instant createdAt;
    private Instant deliveredAt;
    private double total;

    private String firstname;
    private String lastname;
    private String phoneNumber;
    private String address;
    private String zipcode;
    private String city;
    private String deliveryNote;

    private String createdById;
    private UserResponseLightDTO createdBy;

    private List<OrderItem> items;
}
