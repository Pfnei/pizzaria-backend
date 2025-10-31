package at.incrustwetrust.pizzeria.mapper;

import at.incrustwetrust.pizzeria.dto.user.UserCreateDTO;
import at.incrustwetrust.pizzeria.dto.user.UserResponseDTO;
import at.incrustwetrust.pizzeria.dto.user.UserResponseLightDTO;
import at.incrustwetrust.pizzeria.dto.user.UserUpdateDTO;
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
        user.setFirstname(dto.getFirstname());
        user.setLastname(dto.getLastname());
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
        user.setIsActive(true);
        user.setIsAdmin(false);
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

    public static UserResponseDTO toResponseDto(User user) {
        if (user == null) return null;

        UserResponseDTO dto = new UserResponseDTO();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setSalutation(user.getSalutation());
        dto.setSalutationDetail(user.getSalutationDetail());
        dto.setFirstname(user.getFirstname());
        dto.setLastname(user.getLastname());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setAddress(user.getAddress());
        dto.setZipcode(user.getZipcode());
        dto.setCity(user.getCity());
        dto.setCountry(user.getCountry());                     // bereits String (CountryCode.toString)
        dto.setIsActive(user.isActive());
        dto.setIsAdmin(user.isAdmin());
        dto.setCreatedAt(user.getCreatedAt());

        dto.setCreatedById(user.getCreatedBy() != null ? user.getCreatedBy().getUserId() : null);
        dto.setLastUpdatedAt(user.getLastUpdatedAt());
        dto.setLastUpdatedById(user.getLastUpdatedBy() != null ? user.getLastUpdatedBy().getUserId() : null);
        dto.setOrders(user.getOrders() != null ? user.getOrders() : null);

        return dto;
    }

    public static UserResponseLightDTO toResponseLightDto(User user) {
        if (user == null) return null;

        UserResponseLightDTO dto = new UserResponseLightDTO();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setFirstname(user.getFirstname());
        dto.setLastname(user.getLastname());
        dto.setEmail(user.getEmail());
        dto.setActive(user.isActive());
        dto.setAdmin(user.isAdmin());
        return dto;
    }

}
