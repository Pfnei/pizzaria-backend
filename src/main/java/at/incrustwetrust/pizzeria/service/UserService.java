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



import java.util.List;

import java.util.Optional;



@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;
    private final FileStorageService fileService;

    // CREATE

    public UserResponseDTO create(UserCreateDTO dto, SecurityUser principal) {
        throwIfUsernameOrEmailExists(dto);

        User creator = userRepository.findById(principal.getId())
                .orElseThrow(() -> new UserNotFoundException("Creator not found: " + principal.getId()));

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

    public UserResponseDTO read(String id, SecurityUser principal) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("No user found with ID: " + id));
        return mapper.toResponseDto(user);
    }

    public List<UserResponseDTO> readAll() {
        return mapper.toResponseDtoList(userRepository.findAll());
    }


    // UPDATE

    /**
     * Updates user if authorized; enforces admin/self password change
     */
    public UserResponseDTO update(UserUpdateDTO dto, String id, SecurityUser principal) {
        // 1. Existenz prüfen
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("No user found with ID: " + id));

        // 2. Dubletten-Check (Email/Username)
        throwIfUsernameOrEmailExists(dto, id);

        boolean isAdmin = principal.isAdmin();
        boolean isSelf  = principal.getId().equals(id);

        // 3. Sicherheits-Check: Nur Admin ODER der User selbst dürfen weiter
        if (!isAdmin && !isSelf) {
            throw new UnauthorizedActionException("You are not allowed to update this user.");
        }

        // 4. Status-Werte sichern (Nur Admins dürfen Rollen/Status ändern)
        boolean oldAdmin  = existingUser.isAdmin();
        boolean oldActive = existingUser.isActive();

        // 5. Mapping der neuen Daten
        mapper.updateEntity(dto, existingUser);

        // 6. Schutz der Admin-Felder: Wenn kein Admin, alte Werte wiederherstellen
        if (!isAdmin) {
            existingUser.setAdmin(oldAdmin);
            existingUser.setActive(oldActive);
        }

        // 7. Passwort-Update Logik
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        // 8. Meta-Daten setzen (Wer hat zuletzt geändert?)
        User currentUser = userRepository.findById(principal.getId())
                .orElseThrow(() -> new UserNotFoundException("Current user not found"));
        existingUser.setLastUpdatedBy(currentUser);

        // 9. Speichern und zurückgeben
        User saved = userRepository.save(existingUser);
        return mapper.toResponseDto(saved);
    }



    // DELETE

    public UserResponseDTO delete(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("No user found with ID: " + id));

        // Profilbild löschen, falls vorhanden
        if (user.getProfilePicture() != null) {
            fileService.deleteProfileImage(user.getProfilePicture());
        }

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
