package at.incrustwetrust.pizzeria.dto.order;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrderCreateDTO {

    @NotBlank(message = "Vorname darf nicht leer sein")
    private String firstname;

    @NotBlank(message = "Nachname darf nicht leer sein")
    private String lastname;

    @NotBlank(message = "Telefonnummer darf nicht leer sein")
    private String phoneNumber;

    @NotBlank(message = "Adresse darf nicht leer sein")
    private String address;

    @NotBlank(message = "Postleitzahl darf nicht leer sein")
    private String zipcode;

    @NotBlank(message = "Stadt darf nicht leer sein")
    private String city;

    private String deliveryNote;

    @PositiveOrZero(message = "Gesamtbetrag darf nicht negativ sein")
    private double total;

    // Optional: wenn Bestellung einem Benutzer zugeordnet wird
    private String createdById;
}
