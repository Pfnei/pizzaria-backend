package at.incrustwetrust.pizzeria.dto.order;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.Instant;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder @ToString
public class OrderUpdateDTO {
    private String deliveryNote;
    private Instant deliveredAt;
    @PositiveOrZero private Double total;
    private String zipcode;
    private String firstname;
    private String lastname;
    private String city;
    private String address;
    private String phoneNumber;
    private String status; // wird ignoriert
}
