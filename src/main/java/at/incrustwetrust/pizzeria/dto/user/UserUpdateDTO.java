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
    private String firstname;
    private String lastname;
    private Boolean admin;
    private Boolean active;
    private String email;
    private String phoneNumber;
    private String address;
    private String city;
    private String zipcode;
    private String salutation;        // e.g. "MR", "MRS"
    private String salutationDetail;
    private String country;           // e.g. "AT", "DE"
    private String password;

}
