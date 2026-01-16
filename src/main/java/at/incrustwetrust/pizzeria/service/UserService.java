package at.incrustwetrust.pizzeria.service;

import at.incrustwetrust.pizzeria.dto.user.*;
import at.incrustwetrust.pizzeria.entity.User;
import at.incrustwetrust.pizzeria.exception.UnauthorizedActionException;
import at.incrustwetrust.pizzeria.exception.UserAlreadyExistsException;
import at.incrustwetrust.pizzeria.exception.UserNotFoundException;
import at.incrustwetrust.pizzeria.mapper.UserMapper;
import at.incrustwetrust.pizzeria.repository.UserRepository;
import at.incrustwetrust.pizzeria.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;

    // CREATE

    public UserResponseDTO create(UserCreateDTO dto, String createdById) {
        throwIfUsernameOrEmailExists(dto);

        User creator = userRepository.findById(createdById)
                .orElseThrow(() -> new UserNotFoundException("Creator not found: " + createdById));

        // 1. Mapper erstellt die Basis-Entity (createdBy bleibt hier noch leer/ignore)
        User user = mapper.toEntity(dto, null);

        // 2. Passwort setzen
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        // 3. Den Creator manuell als Objekt setzen
        user.setCreatedBy(creator);

        User saved = userRepository.save(user);
        return mapper.toResponseDto(saved);
    }


    // READ

    public UserResponseDTO read(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("No user found with ID: " + id));
        return mapper.toResponseDto(user);
    }

    public List<UserResponseDTO> readAll() {
        return mapper.toResponseDtoList(userRepository.findAll());
    }


    // UPDATE

    public UserResponseDTO update(UserUpdateDTO dto, String id, SecurityUser principal) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(
                        "No user found with ID: " + id
                ));

        throwIfUsernameOrEmailExists(dto, id);

        boolean isAdmin = principal.isAdmin();
        boolean isSelf  = principal.getId().equals(id);

        // Zusätzliche Sicherung, falls PreAuthorize irgendwann geändert wird:
        if (!isAdmin && !isSelf) {
            throw new UnauthorizedActionException("You are not allowed to update this user.");
        }

        // aktuelle Werte sichern, bevor der Mapper drüberbügelt
        boolean oldAdmin  = existingUser.isAdmin();
        boolean oldActive = existingUser.isActive();

        // hier werden alle Felder gemäß DTO → Entity gemappt
        mapper.updateEntity(dto, existingUser);

        // Nur Admin darf admin/active wirklich ändern
        if (!isAdmin) {
            existingUser.setAdmin(oldAdmin);
            existingUser.setActive(oldActive);
        }

        // Passwort-Änderung
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            // nur Admin ODER self dürfen das Passwort ändern
            if (!isAdmin && !isSelf) {
                throw new UnauthorizedActionException("You are not allowed to change the password.");
            }
            existingUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        existingUser.setLastUpdatedBy(
                userRepository.findById(principal.getId())
                        .orElseThrow(() -> new UserNotFoundException("Current user not found"))
        );

        User saved = userRepository.save(existingUser);
        return mapper.toResponseDto(saved);
    }



    // DELETE

    public UserResponseDTO delete(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("No user found with ID: " + id));
        userRepository.delete(user);
        return mapper.toResponseDto(user);
    }


    // DUPLICATE CHECKS

    private void throwIfUsernameOrEmailExists(UserCreateDTO dto) {
        userRepository.findUserByEmail(dto.getEmail()).ifPresent(u -> {
            throw new UserAlreadyExistsException("A user with this email already exists.");
        });
        userRepository.findUserByUsername(dto.getUsername()).ifPresent(u -> {
            throw new UserAlreadyExistsException("A user with this username already exists.");
        });
    }

    private void throwIfUsernameOrEmailExists(UserUpdateDTO dto, String userId) {
        userRepository.findByEmailAndUserIdNot(dto.getEmail(), userId).ifPresent(u -> {
            throw new UserAlreadyExistsException("Another user with this email already exists.");
        });
        userRepository.findUserByUsernameAndUserIdNot(dto.getUsername(), userId).ifPresent(u -> {
            throw new UserAlreadyExistsException("Another user with this username already exists.");
        });
    }

    public Optional<User> findEntityById(String userId) {
        // Nutzt das UserRepository, um die User-Entity zu finden
        return userRepository.findById(userId);
    }
}
