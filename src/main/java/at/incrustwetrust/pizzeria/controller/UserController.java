package at.incrustwetrust.pizzeria.controller;

import at.incrustwetrust.pizzeria.dto.user.UserCreateDTO;
import at.incrustwetrust.pizzeria.dto.user.UserUpdateDTO;
import at.incrustwetrust.pizzeria.dto.user.UserResponseDTO;
import at.incrustwetrust.pizzeria.dto.user.UserResponseLightDTO;
import at.incrustwetrust.pizzeria.security.SecurityUser;
import at.incrustwetrust.pizzeria.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // =====================================================
    // READ ALL (LIGHT DTO)
    // =====================================================
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserResponseLightDTO>> readAll() {
        List<UserResponseLightDTO> users = userService.readAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> readMe() {
        SecurityUser principal = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserResponseDTO user = userService.read(principal.getUserId());
        return ResponseEntity.ok(user);
    }

    // =====================================================
    // READ ONE (FULL DTO)
    // =====================================================

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> read(@PathVariable String id) {
        assertSelfOrAdmin(id);

        UserResponseDTO user = userService.read(id);
        return ResponseEntity.ok(user);
    }

    // =====================================================
    // CREATE
    // =====================================================
    @PreAuthorize("hasRole('ADMIN')")
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

        assertSelfOrAdmin(id);
        UserResponseDTO updated = userService.update(dto, id);
        return ResponseEntity.ok(updated);
    }

    // =====================================================
    // DELETE
    // =====================================================
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponseDTO> delete(@PathVariable String id) {
        UserResponseDTO deleted = userService.delete(id);
        return ResponseEntity.ok(deleted);
    }

    private SecurityUser getPrincipal() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof SecurityUser principal)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No authenticated user");
        }
        return principal;
    }


    private void assertSelfOrAdmin(String userId) {
        SecurityUser principal = getPrincipal();
        boolean isAdmin = principal.isAdmin();
        boolean itself = principal.getUserId().equals(userId);

        if (!isAdmin && !itself) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not allowed to access this resource."
            );
        }
    }

}
