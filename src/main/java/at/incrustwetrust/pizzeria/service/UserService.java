package at.incrustwetrust.pizzeria.service;

import at.incrustwetrust.pizzeria.dto.UserCreateDTO;
import at.incrustwetrust.pizzeria.dto.UserUpdateDTO;
import at.incrustwetrust.pizzeria.entity.User;
import at.incrustwetrust.pizzeria.exception.ObjectAlreadyExistsException;
import at.incrustwetrust.pizzeria.exception.ObjectNotFoundException;
import at.incrustwetrust.pizzeria.mapper.UserMapper;
import at.incrustwetrust.pizzeria.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User create(UserCreateDTO dto, User createdBy) {
        throwIfUsernameOrEmailExists(dto);
        User user = UserMapper.toEntity(dto, createdBy);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        return userRepository.save(user);
    }

    public User read(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("No user found with ID: " + id));
    }

    public List<User> readAll() {
        return userRepository.findAll();
    }

    public User delete(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("No user found with ID: " + id));
        userRepository.delete(user);
        return user;
    }

    public User update(UserUpdateDTO dto, String id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("No user found with ID: " + id));

        throwIfUsernameOrEmailExists(dto, id);

        UserMapper.updateEntity(dto, existingUser);

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        return userRepository.save(existingUser);
    }

    private void throwIfUsernameOrEmailExists(UserCreateDTO dto) {
        userRepository.findUserByEmail(dto.getEmail()).ifPresent(user -> {
            throw new ObjectAlreadyExistsException("A user with this email already exists.");
        });
        userRepository.findUserByUsername(dto.getUsername()).ifPresent(user -> {
            throw new ObjectAlreadyExistsException("A user with this username already exists.");
        });
    }

    private void throwIfUsernameOrEmailExists(UserUpdateDTO dto, String userId) {
        userRepository.findByEmailAndUserIdNot(dto.getEmail(), userId).ifPresent(user -> {
            throw new ObjectAlreadyExistsException("Another user with this email already exists.");
        });
        userRepository.findUserByUsernameAndUserIdNot(dto.getUsername(), userId).ifPresent(user -> {
            throw new ObjectAlreadyExistsException("Another user with this username already exists.");
        });
    }
}