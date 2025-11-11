package at.incrustwetrust.pizzeria.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.File;
import java.time.Instant;
import java.util.List;
import lombok.*;

@Entity
@Table (name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String productId;
    @NotBlank
    @Column (nullable = false, unique = true)
    private String productName;
    private String productDescription;
    private double price;
    @Transient
    private File productPicture;
    private boolean vegetarian;
    @ManyToMany
    @JoinTable(
            name = "products_allergens",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "allergen_abbreviation"))
    private List<Allergen> allergens;
    private String mainCategory;
    private String subCategory;
    private boolean active;
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

    @OneToMany(mappedBy = "product")
    @Schema(hidden = true)
    @JsonIgnore
    private List<OrderItem> orders;




}
