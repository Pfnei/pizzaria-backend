package at.incrustwetrust.pizzeria.repository;

import at.incrustwetrust.pizzeria.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {
    Optional<Order> findAllByCreatedBy_UserId(String userId);
}
