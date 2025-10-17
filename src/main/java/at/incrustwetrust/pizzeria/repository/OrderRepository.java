package at.incrustwetrust.pizzeria.repository;

import at.incrustwetrust.pizzeria.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,String> {
}
