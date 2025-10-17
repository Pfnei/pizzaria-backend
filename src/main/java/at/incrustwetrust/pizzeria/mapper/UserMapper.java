package at.incrustwetrust.pizzeria.mapper;

import at.incrustwetrust.pizzeria.dto.UserCreateDTO;
import at.incrustwetrust.pizzeria.dto.UserUpdateDTO;
import at.incrustwetrust.pizzeria.entity.CountryCode;
import at.incrustwetrust.pizzeria.entity.Salutation;
import at.incrustwetrust.pizzeria.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {


    public static User toEntity(UserCreateDTO dto, User createdBy){
        User user = new User();

        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword()); // encode this later!!
        if (dto.getSalutation() != null && !dto.getSalutation().isBlank()) {
            try {
                Salutation salutationEnum = Salutation.valueOf(dto.getSalutation().toUpperCase().trim());
                user.setSalutation(salutationEnum);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid salutation: " + dto.getSalutation());
            }
        } else {
            user.setSalutation(null); // or Salutation.MR as default
        }


        user.setSalutationDetail(dto.getSalutationDetail());
        user.setFirstname(dto.getFirstName());
        user.setSurname(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setAddress(dto.getAddress());
        user.setZipcode(dto.getZipcode());
        user.setCity(dto.getCity());

        if (dto.getCountry() != null && !dto.getCountry().isBlank()) {
            try {
                CountryCode countryEnum = CountryCode.valueOf(dto.getCountry().toUpperCase().trim());
                user.setCountry(countryEnum); // setter takes enum
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid country code: " + dto.getCountry());
            }
        } else {
            user.setCountry(null); // or CountryCode.AT as default
        }

        user.setCreatedBy(createdBy);
        user.setActive(true);
        user.setAdmin(false);
        return user;
    }

    public static void updateEntity(UserUpdateDTO dto, User user) {
        if (dto.getUsername() != null) user.setUsername(dto.getUsername());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getPhoneNumber() != null) user.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getAddress() != null) user.setAddress(dto.getAddress());
        if (dto.getCity() != null) user.setCity(dto.getCity());
        if (dto.getZipcode() != null) user.setZipcode(dto.getZipcode());
        if (dto.getSalutationDetail() != null) user.setSalutationDetail(dto.getSalutationDetail());

        if (dto.getSalutation() != null && !dto.getSalutation().isBlank()) {
            try {
                Salutation salutationEnum = Salutation.valueOf(dto.getSalutation().toUpperCase().trim());
                user.setSalutation(salutationEnum);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid salutation: " + dto.getSalutation());
            }
        }

        if (dto.getCountry() != null && !dto.getCountry().isBlank()) {
            try {
                CountryCode countryEnum = CountryCode.valueOf(dto.getCountry().toUpperCase().trim());
                user.setCountry(countryEnum);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid country code: " + dto.getCountry());
            }
        }

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(dto.getPassword()); // Hashing im Service!
        }
    }
}
