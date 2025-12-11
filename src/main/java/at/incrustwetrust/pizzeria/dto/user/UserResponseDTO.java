package at.incrustwetrust.pizzeria.dto.user;

import at.incrustwetrust.pizzeria.entity.Order;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserResponseDTO {

    private String userId;
    private String username;
    private String salutation;
    private String salutationDetail;
    private String firstname;
    private String lastname;
    private String email;
    private String phoneNumber;
    private String address;
    private String zipcode;
    private String city;
    private String country;

    private String profilePicture;

    // vereinheitlicht zu einfachem Namen für MapStruct
    private boolean active;
    private boolean admin;

    private Instant createdAt;
    private String createdById;

    private Instant lastUpdatedAt;
    private String lastUpdatedById;

    private List<Order> orders;
}
