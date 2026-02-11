package at.incrustwetrust.pizzeria.dto.product;

import at.incrustwetrust.pizzeria.dto.user.UserResponseLightDTO;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProductDeleteDTO {
    private String productId;
    private String productName;
    
}
