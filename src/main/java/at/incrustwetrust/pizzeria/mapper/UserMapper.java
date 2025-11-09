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

            // DTO verwendet 'active/admin' (primitive), Entity verwendet 'active/admin' (primitive)
            // Automatisches Mapping funktioniert jetzt besser.
            @Mapping(target = "active", expression = "java(true)"),
            @Mapping(target = "admin",  expression = "java(false)"),
    })
    User toEntity(UserCreateDTO dto, @Context User createdBy);

    // ======= UPDATE / PATCH (FÜGT BOOLEAN-AKTUALISIERUNG HINZU) =======
    // HINWEIS: Hier sollte das DTO Boolean-Wrapper (Boolean) verwenden,
    //          aber da Sie die DTO-Änderung nicht wollten, lassen wir es mit String-Konverter laufen.
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

            // NEU: Konvertiere String DTO auf boolean Entity für Status-Änderungen.
            // Beachten Sie, dass die DTOs 'active' und 'admin' als String verwenden.
            @Mapping(target = "active", source = "active", qualifiedByName = "stringToBoolean"),
            @Mapping(target = "admin",  source = "admin", qualifiedByName = "stringToBoolean")
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
    // Hier ist kein Mapping mehr nötig, da die Entity-Getter isActive/isAdmin auf DTO active/admin mappen.
    UserResponseLightDTO toResponseLightDto(User u);

    List<UserResponseDTO> toResponseDtoList(List<User> users);
    List<UserResponseLightDTO> toResponseLightDtoList(List<User> users);

    // ======= Konverter (Hinzugefügt: String-zu-Boolean Konverter) =======
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

    /**
     * Konvertiert String ("true"/"false") aus dem Update DTO in primitive boolean der Entity.
     */
    @Named("stringToBoolean")
    default boolean stringToBoolean(String src) {
        if (src == null) {
            // Null-Strings werden dank NullValuePropertyMappingStrategy.IGNORE ignoriert (kein Update).
            // Da MapStruct jedoch einen primitiven Rückgabewert erwartet, geben wir einen sicheren Wert zurück.
            // MapStruct sollte diesen Rückgabewert aufgrund der Mapping-Strategie ignorieren.
            return false;
        }
        String s = src.trim().toLowerCase();
        if ("true".equals(s) || "1".equals(s)) return true;
        if ("false".equals(s) || "0".equals(s)) return false;

        throw new IllegalArgumentException("Invalid boolean value for active/admin: " + src);
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