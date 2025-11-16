package at.incrustwetrust.pizzeria.dto.product;

import java.util.List;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProductCreateDTO {
    private String productName;
    private String productDescription;
    private Double price;
    private boolean vegetarian;
    private boolean active;
    private String mainCategory;
    private String subCategory;
    private List<String> allergens;
}
