package at.incrustwetrust.pizzeria.dto.order;

import at.incrustwetrust.pizzeria.dto.product.ProductResponseDTO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrderItemResponseDTO {
    private String orderItemId;
    private ProductResponseDTO product;
    private String productName;
    private int quantity;
    private double price;
}
