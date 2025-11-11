package at.incrustwetrust.pizzeria.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.*;
import lombok.*; // 🟢 WICHTIG: Lombok Import

import java.io.File;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "users")
@Getter // 🟢 Fügt alle Getter (getUserId(), isActive(), isAdmin() etc.) hinzu
@Setter // 🟢 Fügt alle Setter (setUserId(), setIsActive() etc.) hinzu
@NoArgsConstructor // 🟢 Ersetzt den leeren Konstruktor
// Optional: @AllArgsConstructor (Wenn Sie einen Konstruktor mit allen Feldern wünschen)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String userId;

    @Transient
    private File profilPicture;

    @NotBlank
    @Column (nullable = false, unique = true)
    @Size(min = 5 , message = "mindestens 5 Zeichen erforderlich")
    @Size(max = 30, message = "Maximale Länge = 30 Zeichen")
    private String username;

    @NotBlank
    @Column (nullable = false)
    @Size(min = 12 , message = "mindestens 12 Zeichen erforderlich")
    @Pattern(regexp = ".*\\d.*", message = "mindestens eine Zahl erforderlich")
    @Pattern(regexp = ".*[A-Z].*", message = "mindestens eine Grossbuchstabe erforderlich")
    @Pattern(regexp = ".*[a-z].*", message = "mindestens eine Kleinbuchstabe erforderlich")
    @Pattern(regexp = ".*[@$!%*?&].*", message = "mindestens eine Sonderzeichen erforderlich")
    private String password;

    private String salutation;
    @Size(min = 4 , message = "mindestens 4 Zeichen erforderlich")
    @Size(max = 30, message = "Maximale Länge = 30 Zeichen")
    @Column(length = 30)
    private String salutationDetail;
    // ... (Weitere String Felder)
    private String firstname;
    private String lastname;

    @Email
    @NotBlank
    @Size(min = 5 , message = "mindestens 5 Zeichen erforderlich")
    @Size(max = 100, message = "Maximale Länge = 100 Zeichen")
    @Column (nullable = false, unique = true)
    private String email;


    private String phoneNumber;
    private String address;
    @Size(min = 2, max = 10)
    @Column(length = 10)
    private String zipcode;
    private String city;
    private String country;


    private boolean isActive = true;
    private boolean isAdmin = false;

    @CreationTimestamp
    private Instant createdAt;

    @ManyToOne
    @Schema(hidden = true)
    @JsonIgnore
    private User createdBy;

    @UpdateTimestamp
    private Instant lastUpdatedAt;

    @ManyToOne
    @Schema(hidden = true)
    @JsonIgnore
    private User lastUpdatedBy;

    @OneToMany(mappedBy = "createdBy")
    private List<Order> orders;




}