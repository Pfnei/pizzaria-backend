package at.incrustwetrust.pizzeria.repository;

import at.incrustwetrust.pizzeria.entity.Allergen;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AllergenRepository extends JpaRepository<Allergen, String> {
}
