package at.incrustwetrust.pizzeria.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.List;


@Entity
@Table (name = "allergens")
public class Allergen {

   @Id
   private char abbreviation;
   @NotBlank
   @Column (nullable = false)
   private String description;
   @CreationTimestamp
   private Instant createdAt;
   @ManyToOne
   private User createdBy;
   @UpdateTimestamp
   private Instant lastUpdatedAt;
   @ManyToOne
   private User lastUpdatedBy;

   @ManyToMany (mappedBy = "allergens")
   private List<Product> products;

   public Allergen(){};
   
   public Allergen(AllergenType allergenType, User createdBy) {
      this.abbreviation = allergenType.name().toString().charAt(0);
      this.description = allergenType.getDescription();
      this.createdBy = createdBy;
   }

   public char getAbbreviation() {
      return abbreviation;
   }

   public void setAbbreviation( AllergenType allergenType) {
      this.abbreviation = allergenType.name().toString().charAt(0);
      this.description = allergenType.getDescription();

   }

   public String getDescription() {
      return description;
   }

   
}



