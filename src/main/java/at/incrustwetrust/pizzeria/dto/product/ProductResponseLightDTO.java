package at.incrustwetrust.pizzeria.dto.product;

import at.incrustwetrust.pizzeria.dto.user.UserResponseLightDTO;
import at.incrustwetrust.pizzeria.entity.Allergen;

import java.time.Instant;
import java.util.List;

public class ProductResponseLightDTO {

    private String productId;
    private String productName;
    private double price;

    private String mainCategory;
    private String subCategory;

    private boolean isVegetarian;
    private boolean isActive;

    private List<Allergen> allergens;

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getMainCategory() { return mainCategory; }
    public void setMainCategory(String mainCategory) { this.mainCategory = mainCategory; }

    public String getSubCategory() { return subCategory; }
    public void setSubCategory(String subCategory) { this.subCategory = subCategory; }

    public boolean isVegetarian() { return isVegetarian; }
    public void setIsVegetarian(boolean vegetarian) { isVegetarian = vegetarian; }

    public boolean isActive() { return isActive; }
    public void setIsActive(boolean active) { isActive = active; }

    public List<Allergen> getAllergens() { return allergens; }
    public void setAllergens(List<Allergen> allergens) { this.allergens = allergens; }

}
