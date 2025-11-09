package at.incrustwetrust.pizzeria.dto.product;

import at.incrustwetrust.pizzeria.dto.user.UserResponseLightDTO;
import lombok.*;
import java.time.Instant;
import java.util.List;
// ProductResponseDTO
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder @ToString
public class ProductResponseDTO {
    private String productId;
    private String productName;
    private String productDescription;
    private Double price;


    private boolean vegetarian;
    private boolean active;

    private String mainCategory;
    private String subCategory;
    private List<String> allergens;

    private Instant createdAt;
    private String createdById;
    private UserResponseLightDTO createdBy;

    private Instant lastUpdatedAt;
    private String lastUpdatedById;
    private UserResponseLightDTO lastUpdatedBy;
}