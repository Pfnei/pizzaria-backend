package at.incrustwetrust.pizzeria.mapper;

import at.incrustwetrust.pizzeria.dto.user.*;
import at.incrustwetrust.pizzeria.entity.*;
import org.mapstruct.*;
import java.util.List;

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

            // DTO verwendet 'active/isAdmin' (primitive), Entity verwendet 'active/isAdmin' (primitive)
            // Automatisches Mapping funktioniert jetzt besser.
            @Mapping(target = "active", expression = "java(true)"),
            @Mapping(target = "admin",  expression = "java(false)"),
    })
    User toEntity(UserCreateDTO dto, @Context User createdBy);

    // ======= UPDATE / PATCH (BEREINIGT) =======
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "userId", ignore = true),
            @Mapping(target = "profilPicture", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "lastUpdatedAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "lastUpdatedBy", ignore = true),
            @Mapping(target = "orders", ignore = true),

            // Strings -> Enums
            @Mapping(target = "salutation", source = "salutation", qualifiedByName = "toSalutation"),
            @Mapping(target = "country", source = "country", qualifiedByName = "toCountry"),

            // ⛔ HIER WERDEN DIE ZWEI MAPPINGS FÜR isActive/isAdmin ENTFERNT
            // MapStruct handhabt das Mapping von Boolean (DTO) zu boolean (Entity) automatisch.
            // Die Felder werden ignoriert, wenn sie im DTO null sind (dank NullValuePropertyMappingStrategy.IGNORE).
    })
    void updateEntity(UserUpdateDTO dto, @MappingTarget User user);

    // ======= READ: ENTITY → DTO (VEREINFACHT) =======
    @Mappings({
            // Automatisches Mapping funktioniert, keine expliziten Booleans nötig, wenn Namen übereinstimmen.
            @Mapping(target = "createdById",
                    expression = "java(u.getCreatedBy()!=null ? u.getCreatedBy().getUserId() : null)"),
            @Mapping(target = "lastUpdatedById",
                    expression = "java(u.getLastUpdatedBy()!=null ? u.getLastUpdatedBy().getUserId() : null)"),

            @Mapping(target = "orders", ignore = true)
    })
    UserResponseDTO toResponseDto(User u);

    // ======= READ: ENTITY → LIGHT DTO (VEREINFACHT) =======
    // Hier ist kein Mapping mehr nötig, da die Entity-Getter isActive/isAdmin auf DTO active/isAdmin mappen.
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