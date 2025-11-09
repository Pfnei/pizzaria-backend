package at.incrustwetrust.pizzeria.dto.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserCreateDTO {

    private String username;
    private String password;
    private String salutation;        // z. B. "MR", "MRS"
    private String salutationDetail;
    private String firstname;
    private String lastname;
    private String email;
    private String phoneNumber;
    private String address;
    private String zipcode;
    private String city;
    private String country;           // z. B. "AT", "DE"
}
