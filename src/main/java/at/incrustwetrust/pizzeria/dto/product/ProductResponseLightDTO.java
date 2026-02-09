package at.incrustwetrust.pizzeria.dto.product;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProductResponseLightDTO {
    private String productId;
    private String productName;
    private Double price;
    private String productPicture;

    private boolean vegetarian;
    private boolean active;

    private String mainCategory;
    private String subCategory;
    private List<String> allergens;
}
