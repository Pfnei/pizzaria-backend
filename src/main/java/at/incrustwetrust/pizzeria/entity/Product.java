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

@Entity
@Table (name = "products")
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
    private boolean isVegetarian;
    @ManyToMany
    @JoinTable(
            name = "products_allergens",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "allergen_abbreviation"))
    private List<Allergen> allergens;
    private String mainCategory;
    private String subCategory;
    private boolean isActive;
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

    public Product (){};

    public Product(String productName, String productDescription, double price, File productPicture, boolean isVegetarian, List<Allergen> allergens, MainCategory mainCategory, SubCategory subCategory, boolean isActive, User createdBy, List<OrderItem> orders) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.price = price;
        this.productPicture = productPicture;
        this.isVegetarian = isVegetarian;
        this.allergens = allergens;
        this.mainCategory = mainCategory.toString();
        this.subCategory = subCategory.toString();
        this.isActive = isActive;
        this.createdBy = createdBy;
        this.orders = orders;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public File getProductPicture() {
        return productPicture;
    }

    public void setProductPicture(File productPicture) {
        this.productPicture = productPicture;
    }

    public boolean isVegetarian() {
        return isVegetarian;
    }

    public void setVegetarian(boolean vegetarian) {
        isVegetarian = vegetarian;
    }

    public List<Allergen> getAllergens() {
        return allergens;
    }

    public void setAllergens(List<Allergen> allergens) {
        this.allergens = allergens;
    }
    
   
    public String getMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(MainCategory mainCategory) {
        this.mainCategory = mainCategory.toString();
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(SubCategory subCategory) {
        this.subCategory = subCategory.toString();
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }



    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(Instant lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public User getLastUpdatedBy() {
        return lastUpdatedBy;
    }


    public List<OrderItem> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderItem> orders) {
        this.orders = orders;
    }
}
