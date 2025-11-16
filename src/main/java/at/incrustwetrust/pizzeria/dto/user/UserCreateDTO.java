package at.incrustwetrust.pizzeria.dto.user;


import jakarta.validation.constraints.*;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserCreateDTO {

    @NotBlank
    @Size(min = 5, max = 30, message = "Username muss zwischen 5 und 30 Zeichen lang sein")
    private String username;

    @NotBlank
    @Size(min = 12, message = "Passwort muss mindestens 12 Zeichen lang sein")
    @Pattern(regexp = ".*\\d.*", message = "Passwort benötigt mindestens eine Zahl")
    @Pattern(regexp = ".*[A-Z].*", message = "Passwort benötigt mindestens einen Großbuchstaben")
    @Pattern(regexp = ".*[a-z].*", message = "Passwort benötigt mindestens einen Kleinbuchstaben")
    @Pattern(regexp = ".*[@$!%*?&].*", message = "Passwort benötigt mindestens ein Sonderzeichen (@$!%*?&)")
    private String password;

    private String salutation;
    private String salutationDetail;

    @NotBlank
    private String firstname;

    @NotBlank
    private String lastname;

    @Email
    @NotBlank
    @Size(min = 5, max = 100)
    private String email;

    private String phoneNumber;
    private String address;

    @Size(min = 2, max = 10)
    private String zipcode;

    private String city;
    private String country;

    // Für Create kannst du die eigentlich auch weglassen,
    // oder vom Backend setzen (immer active=true, admin=false)
    private Boolean active;
    private Boolean admin;
}
