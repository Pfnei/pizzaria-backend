package at.incrustwetrust.pizzeria.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProductCreateDTO {
    @NotBlank(message = "Product name must not be empty")
    private String productName;
    private String productDescription;
    private Double price;
    private String productPicture;
    private boolean vegetarian;
    private boolean active;
    private String mainCategory;
    private String subCategory;
    private List<String> allergens;
}
