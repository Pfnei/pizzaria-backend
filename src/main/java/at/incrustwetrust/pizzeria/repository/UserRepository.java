package at.incrustwetrust.pizzeria.repository;

import at.incrustwetrust.pizzeria.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional <User> findUserByEmail(String email);
    Optional <User> findUserByUsername(String username);
    // make sure that Email / Username does not exit BUT don't compare it with the own User`s mail / Username
    Optional <User> findByEmailAndUserIdNot(String email,String id);
    Optional <User> findUserByUsernameAndUserIdNot(String username, String id);
}
