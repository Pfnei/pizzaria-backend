package at.incrustwetrust.pizzeria.dto.product;

import lombok.*;

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
    private Boolean vegetarian;
    private Boolean active;
    private String mainCategory;
    private String subCategory;
    private java.util.List<String> allergens;
}
