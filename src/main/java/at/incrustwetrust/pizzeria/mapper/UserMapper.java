package at.incrustwetrust.pizzeria.mapper;

import at.incrustwetrust.pizzeria.dto.user.*;
import at.incrustwetrust.pizzeria.entity.*;
import org.mapstruct.*;
import java.util.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // ======= CREATE =======
    @Mappings({
            @Mapping(target = "userId", ignore = true),
            @Mapping(target = "profilPicture", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "lastUpdatedAt", ignore = true),
            @Mapping(target = "lastUpdatedBy", ignore = true),
            @Mapping(target = "orders", ignore = true),

            @Mapping(target = "salutation", source = "salutation", qualifiedByName = "toSalutation"),
            @Mapping(target = "country", source = "country", qualifiedByName = "toCountry"),
            @Mapping(target = "createdBy", ignore = true),

            @Mapping(target = "isActive", expression = "java(true)"),
            @Mapping(target = "isAdmin", expression = "java(false)")
    })
    User toEntity(UserCreateDTO dto, @Context User createdBy);

    // ======= UPDATE / PATCH =======
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "userId", ignore = true),
            @Mapping(target = "profilPicture", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "lastUpdatedAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "lastUpdatedBy", ignore = true),
            @Mapping(target = "orders", ignore = true),
            @Mapping(target = "salutation", source = "salutation", qualifiedByName = "toSalutation"),
            @Mapping(target = "country", source = "country", qualifiedByName = "toCountry")
    })
    void updateEntity(UserUpdateDTO dto, @MappingTarget User user);

    // ======= READ: ENTITY → DTO =======
    @Mappings({
            @Mapping(target = "isActive", source = "active"),
            @Mapping(target = "isAdmin", source = "admin"),
            @Mapping(target = "createdById",
                    expression = "java(u.getCreatedBy()!=null ? u.getCreatedBy().getUserId() : null)"),
            @Mapping(target = "lastUpdatedById",
                    expression = "java(u.getLastUpdatedBy()!=null ? u.getLastUpdatedBy().getUserId() : null)"),
            @Mapping(target = "orders", ignore = true)
    })
    UserResponseDTO toResponseDto(User u);

    @Mappings({
            @Mapping(target = "active", source = "active"),
            @Mapping(target = "admin", source = "admin")
    })
    UserResponseLightDTO toResponseLightDto(User u);

    List<UserResponseDTO> toResponseDtoList(List<User> users);
    List<UserResponseLightDTO> toResponseLightDtoList(List<User> users);

    // ======= Konverter =======
    @Named("toSalutation")
    default Salutation toSalutation(String src) {
        if (src == null || src.isBlank()) return null;
        try { return Salutation.valueOf(src.trim().toUpperCase()); }
        catch (Exception e) { throw new IllegalArgumentException("Invalid salutation: " + src); }
    }

    @Named("toCountry")
    default CountryCode toCountry(String src) {
        if (src == null || src.isBlank()) return null;
        try { return CountryCode.valueOf(src.trim().toUpperCase()); }
        catch (Exception e) { throw new IllegalArgumentException("Invalid country code: " + src); }
    }

    // ======= Nachbearbeitung =======
    @AfterMapping
    default void afterMapping(@MappingTarget User u, @Context User createdBy) {
        if (createdBy != null) u.setCreatedBy(createdBy);
        if (u.getUsername()!=null)  u.setUsername(u.getUsername().trim());
        if (u.getEmail()!=null)     u.setEmail(u.getEmail().trim());
        if (u.getFirstname()!=null) u.setFirstname(u.getFirstname().trim());
        if (u.getLastname()!=null)  u.setLastname(u.getLastname().trim());
        if (u.getCity()!=null)      u.setCity(u.getCity().trim());
    }
}
