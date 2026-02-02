package at.incrustwetrust.pizzeria.dto.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrderItemCreateDTO {

    @NotBlank
    private String productId;

    @Min(1)
    private int quantity;
}
