package at.incrustwetrust.pizzeria.dto.product;

import at.incrustwetrust.pizzeria.dto.user.UserResponseLightDTO;
import at.incrustwetrust.pizzeria.entity.Allergen;

import java.time.Instant;
import java.util.List;

public class ProductResponseDTO {

    private String productId;
    private String productName;
    private String productDescription;
    private double price;

    private boolean isVegetarian;
    private boolean isActive;

    private String mainCategory;
    private String subCategory;

    private List<Allergen> allergens;

    private Instant createdAt;
    private String createdById;
    private UserResponseLightDTO createdBy;

    private Instant lastUpdatedAt;
    private String lastUpdatedById;
    private UserResponseLightDTO lastUpdatedBy;

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getProductDescription() { return productDescription; }
    public void setProductDescription(String productDescription) { this.productDescription = productDescription; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public boolean isVegetarian() { return isVegetarian; }
    public void setIsVegetarian(boolean vegetarian) { isVegetarian = vegetarian; }

    public boolean isActive() { return isActive; }
    public void setIsActive(boolean active) { isActive = active; }

    public String getMainCategory() { return mainCategory; }
    public void setMainCategory(String mainCategory) { this.mainCategory = mainCategory; }

    public String getSubCategory() { return subCategory; }
    public void setSubCategory(String subCategory) { this.subCategory = subCategory; }

    public List<Allergen> getAllergens() { return allergens; }
    public void setAllergens(List<Allergen> allergens) { this.allergens = allergens; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public String getCreatedById() { return createdById; }
    public void setCreatedById(String createdById) { this.createdById = createdById; }

    public UserResponseLightDTO getCreatedBy() { return createdBy; }
    public void setCreatedBy(UserResponseLightDTO createdBy) { this.createdBy = createdBy; }

    public Instant getLastUpdatedAt() { return lastUpdatedAt; }
    public void setLastUpdatedAt(Instant lastUpdatedAt) { this.lastUpdatedAt = lastUpdatedAt; }

    public String getLastUpdatedById() { return lastUpdatedById; }
    public void setLastUpdatedById(String lastUpdatedById) { this.lastUpdatedById = lastUpdatedById; }

    public UserResponseLightDTO getLastUpdatedBy() { return lastUpdatedBy; }
    public void setLastUpdatedBy(UserResponseLightDTO lastUpdatedBy) { this.lastUpdatedBy = lastUpdatedBy; }
}
