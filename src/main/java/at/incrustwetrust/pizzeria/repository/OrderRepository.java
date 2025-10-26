package at.incrustwetrust.pizzeria.repository;

import at.incrustwetrust.pizzeria.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,String> {

    List<Order> findAllByCreatedByUserId(String userId);

}
