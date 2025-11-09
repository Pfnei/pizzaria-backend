package at.incrustwetrust.pizzeria.dto.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserUpdateDTO {

    private String username;
    private String email;
    private String phoneNumber;
    private String address;
    private String city;
    private String zipcode;
    private String salutation;        // z. B. "MR", "MRS"
    private String salutationDetail;
    private String country;           // z. B. "AT", "DE"
    private String password;
}
