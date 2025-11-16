package at.incrustwetrust.pizzeria.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "allergens")
@Getter
@Setter
@NoArgsConstructor
public class Allergen {

    // =====================================================
    // ID (praktischer Primärschlüssel)
    // =====================================================
    @Id
    @Column(length = 5) // z. B. "A", "B", "GLU", "LAC"
    private String abbreviation;

    @NotBlank
    @Size(max = 255)
    @Column(nullable = false)
    private String description;

    @CreationTimestamp
    private Instant createdAt;

    @ManyToOne
    private User createdBy;

    @UpdateTimestamp
    private Instant lastUpdatedAt;

    @ManyToOne
    private User lastUpdatedBy;

    @ManyToMany(mappedBy = "allergens")
    private List<Product> products;

    // =====================================================
    // Convenience-Konstruktor
    // =====================================================
    public Allergen(String abbreviation, String description, User createdBy) {
        this.abbreviation = abbreviation.toUpperCase();
        this.description = description;
        this.createdBy = createdBy;
    }
}
