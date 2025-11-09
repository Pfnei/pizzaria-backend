package at.incrustwetrust.pizzeria.dto.order;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrderUpdateDTO {

    // Optionale Felder, die Admins updaten dürfen
    private String deliveryNote;

    private Instant deliveredAt; // Zeitpunkt der Lieferung

    @PositiveOrZero(message = "Gesamtbetrag darf nicht negativ sein")
    private Double total;

    private String city;
    private String address;
    private String phoneNumber;

    // z. B. neuer Status (optional)
    private String status;
}
