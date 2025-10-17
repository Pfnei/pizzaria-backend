package at.incrustwetrust.pizzeria.repository;

import at.incrustwetrust.pizzeria.entity.Product;
import at.incrustwetrust.pizzeria.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository <Product, String> {

    Optional <Product> findProductByProductName(String productName);
    // make sure that productname does not exit BUT don't compare it with the own product´s own productname
    Optional <Product> findProductByProductNameAndProductIdNot(String productName, String id);

}
