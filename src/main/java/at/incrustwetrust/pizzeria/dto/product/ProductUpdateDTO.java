package at.incrustwetrust.pizzeria.dto.product;

import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProductUpdateDTO {
    private String productName;
    private String productDescription;
    private Double price;
    private String productPicture;
    private Boolean vegetarian;
    private Boolean active;
    private String mainCategory;
    private String subCategory;
    private List<String> allergens;
}
