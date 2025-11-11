package at.incrustwetrust.pizzeria.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.*;

@Entity
@Table (name = "order_items")
@Getter
@Setter
@NoArgsConstructor

public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String orderItemId;

    @ManyToOne
    @JoinColumn (name = "order_id")
    @Schema(hidden = true)
    @JsonIgnore
    private Order order;
    @ManyToOne
    @JoinColumn  (name = "product_id")
    private Product product;
    @NotBlank
    @Column (nullable = false)
    private String productName;
    @NotNull
    @Column (nullable = false)
    private int quantity;
    // = quantity * price/unit
    @NotNull
    @Column (nullable = false)
    private double price;



}
