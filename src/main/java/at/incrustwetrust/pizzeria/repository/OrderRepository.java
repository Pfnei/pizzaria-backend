package at.incrustwetrust.pizzeria.repository;

import at.incrustwetrust.pizzeria.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    // Alle Bestellungen eines bestimmten Users finden (via createdBy.userId)
    List<Order> findAllByCreatedBy_UserId(String userId);
}
