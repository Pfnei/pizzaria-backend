package at.incrustwetrust.pizzeria.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String userId;

    // Wird nicht in der DB gespeichert – nur temporär
    @Transient
    private String profilPicture;

    @Column(nullable = false, unique = true, length = 30)
    private String username;

    @Column(nullable = false)
    private String password;

    // z. B. "MR", "MRS" – kannst du später auch auf Enum ändern
    private String salutation;

    @Column(length = 30)
    private String salutationDetail;

    private String firstname;
    private String lastname;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    private String phoneNumber;
    private String address;

    @Column(length = 10)
    private String zipcode;

    private String city;
    private String country;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false)
    private boolean admin = false;

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
