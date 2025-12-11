package at.incrustwetrust.pizzeria.controller;

import at.incrustwetrust.pizzeria.dto.user.UserCreateDTO;
import at.incrustwetrust.pizzeria.dto.user.UserUpdateDTO;
import at.incrustwetrust.pizzeria.dto.user.UserResponseDTO;
import at.incrustwetrust.pizzeria.security.SecurityUser;
import at.incrustwetrust.pizzeria.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;


import java.util.List;
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> readAll() {
        List<UserResponseDTO> users = userService.readAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> readMe(@AuthenticationPrincipal SecurityUser principal ) {

        UserResponseDTO user = userService.read(principal.getId());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> readById(@PathVariable String id) {
        UserResponseDTO user = userService.read(id);
        return ResponseEntity.ok(user);
    }



    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> update(
            @PathVariable String id,
            @Valid @RequestBody UserUpdateDTO dto,
            @AuthenticationPrincipal SecurityUser principal) {

        UserResponseDTO updated = userService.update(dto, id, principal);
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@Valid @RequestBody UserCreateDTO dto) {
        UserResponseDTO created = userService.create(dto, null);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    } // muss noch mit @AuthenticationPrincipal SecurityUser principal erweitert werden fürs etzen beim user wer created hat.



    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponseDTO> delete(@PathVariable String id) {
        UserResponseDTO deleted = userService.delete(id);
        return ResponseEntity.ok(deleted);
    }


}
