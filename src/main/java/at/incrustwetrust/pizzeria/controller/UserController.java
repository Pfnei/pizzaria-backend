package at.incrustwetrust.pizzeria.controller;

import at.incrustwetrust.pizzeria.dto.user.UserCreateDTO;
import at.incrustwetrust.pizzeria.dto.user.UserUpdateDTO;
import at.incrustwetrust.pizzeria.dto.user.UserResponseDTO;
import at.incrustwetrust.pizzeria.dto.user.UserResponseLightDTO;
import at.incrustwetrust.pizzeria.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // =====================================================
    // READ ALL (LIGHT DTO)
    // =====================================================
    @GetMapping
    public ResponseEntity<List<UserResponseLightDTO>> readAll() {
        List<UserResponseLightDTO> users = userService.readAll();
        return ResponseEntity.ok(users);
    }

    // =====================================================
    // READ ONE (FULL DTO)
    // =====================================================
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> read(@PathVariable String id) {
        UserResponseDTO user = userService.read(id);
        return ResponseEntity.ok(user);
    }

    // =====================================================
    // CREATE
    // =====================================================
    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@Valid @RequestBody UserCreateDTO dto) {
        UserResponseDTO created = userService.create(dto, null);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // =====================================================
    // UPDATE (PATCH)
    // =====================================================
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(
            @PathVariable String id,
            @Valid @RequestBody UserUpdateDTO dto) {

        UserResponseDTO updated = userService.update(dto, id);
        return ResponseEntity.ok(updated);
    }

    // =====================================================
    // DELETE
    // =====================================================
    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponseDTO> delete(@PathVariable String id) {
        UserResponseDTO deleted = userService.delete(id);
        return ResponseEntity.ok(deleted);
    }
}
