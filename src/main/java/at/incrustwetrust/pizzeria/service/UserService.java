package at.incrustwetrust.pizzeria.service;

import at.incrustwetrust.pizzeria.dto.user.*;
import at.incrustwetrust.pizzeria.entity.User;
import at.incrustwetrust.pizzeria.mapper.UserMapper;
import at.incrustwetrust.pizzeria.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;

    // ======================================================
    // CREATE
    // ======================================================
    public UserResponseDTO create(UserCreateDTO dto, User createdBy) {
        throwIfUsernameOrEmailExists(dto);

        User user = mapper.toEntity(dto, createdBy);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        User saved = userRepository.save(user);
        return mapper.toResponseDto(saved);
    }

    // ======================================================
    // READ
    // ======================================================
    public UserResponseDTO read(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "No user found with ID: " + id));
        return mapper.toResponseDto(user);
    }

    public List<UserResponseLightDTO> readAll() {
        return mapper.toResponseLightDtoList(userRepository.findAll());
    }

    // ======================================================
    // UPDATE
    // ======================================================
    public UserResponseDTO update(UserUpdateDTO dto, String id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "No user found with ID: " + id));

        throwIfUsernameOrEmailExists(dto, id);

        mapper.updateEntity(dto, existingUser);

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        User saved = userRepository.save(existingUser);
        return mapper.toResponseDto(saved);
    }

    // ======================================================
    // DELETE
    // ======================================================
    public UserResponseDTO delete(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "No user found with ID: " + id));
        userRepository.delete(user);
        return mapper.toResponseDto(user);
    }

    // ======================================================
    // DUPLICATE CHECKS
    // ======================================================
    private void throwIfUsernameOrEmailExists(UserCreateDTO dto) {
        userRepository.findUserByEmail(dto.getEmail()).ifPresent(u -> {
            throw new ResponseStatusException(CONFLICT, "A user with this email already exists.");
        });
        userRepository.findUserByUsername(dto.getUsername()).ifPresent(u -> {
            throw new ResponseStatusException(CONFLICT, "A user with this username already exists.");
        });
    }

    private void throwIfUsernameOrEmailExists(UserUpdateDTO dto, String userId) {
        userRepository.findByEmailAndUserIdNot(dto.getEmail(), userId).ifPresent(u -> {
            throw new ResponseStatusException(CONFLICT, "Another user with this email already exists.");
        });
        userRepository.findUserByUsernameAndUserIdNot(dto.getUsername(), userId).ifPresent(u -> {
            throw new ResponseStatusException(CONFLICT, "Another user with this username already exists.");
        });
    }
}
