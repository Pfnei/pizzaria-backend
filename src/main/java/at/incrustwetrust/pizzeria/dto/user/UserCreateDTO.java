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
    @Size(min = 5, max = 30, message = "Username must be between 5 and 30 characters long")
    private String username;

    @NotBlank
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = ".*\\d.*", message = "Password requires at least one digit")
    @Pattern(regexp = ".*[A-Z].*", message = "Password requires at least one uppercase letter")
    @Pattern(regexp = ".*[a-z].*", message = "Password requires at least one lowercase letter")
    @Pattern(regexp = ".*[!@#$%^&*(),.?\":{}|<>].*", message = "Password requires at least one special character (@$!%*?&)")
    private String password;

    private String salutation;
    private String salutationDetail;
    
    private String firstname;

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

    // For create, you can actually leave them out,
    // or have them set by the backend (always active=true, admin=false)
    private Boolean active;
    private Boolean admin;
}
