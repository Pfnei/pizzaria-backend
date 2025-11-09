package at.incrustwetrust.pizzeria.dto.order;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder @ToString
public class OrderCreateDTO {

    @NotBlank private String firstname;
    @NotBlank private String lastname;
    @NotBlank private String phoneNumber;
    @NotBlank private String address;
    @NotBlank private String zipcode;
    @NotBlank private String city;

    private String deliveryNote;

    @PositiveOrZero private double total;

    // optional: Zuordnung zu User
    private String createdById;
}
