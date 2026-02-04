package at.incrustwetrust.pizzeria.dto.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserResponseLightDTO {
    private String userId;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String phoneNumber;
    private String address;
    private String zipcode;
    private String city;
    private String country;
    private String profilePicture;
    private boolean active;
    private boolean admin;
}
