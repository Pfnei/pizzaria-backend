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
    private boolean active;
    private boolean admin;
    private String zipcode;
    private String profilePicture;

    public UserResponseLightDTO(long l, String mail) {

    }
}
