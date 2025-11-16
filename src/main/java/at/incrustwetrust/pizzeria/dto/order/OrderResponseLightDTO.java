package at.incrustwetrust.pizzeria.dto.order;

import at.incrustwetrust.pizzeria.dto.user.UserResponseLightDTO;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrderResponseLightDTO {

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
}
