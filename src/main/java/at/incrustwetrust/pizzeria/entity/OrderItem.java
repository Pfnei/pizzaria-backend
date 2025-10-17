package at.incrustwetrust.pizzeria.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
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

    public OrderItem () {};

    public OrderItem(Order order, Product product, String productName, int quantity, double price) {
        this.order = order;
        this.product = product;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
