package at.incrustwetrust.pizzeria.controller;

import at.incrustwetrust.pizzeria.dto.user.UserCreateDTO;
import at.incrustwetrust.pizzeria.dto.user.UserUpdateDTO;
import at.incrustwetrust.pizzeria.dto.user.UserResponseDTO;
import at.incrustwetrust.pizzeria.entity.User;
import at.incrustwetrust.pizzeria.mapper.UserMapper;
import at.incrustwetrust.pizzeria.security.SecurityUser;
import at.incrustwetrust.pizzeria.service.CurrentUserService;
import at.incrustwetrust.pizzeria.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final CurrentUserService currentUserService;
    private final UserMapper userMapper;


    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMyProfile() {
        // Uses your CurrentUserService to get the entity from the context
        User currentUser = currentUserService.getCurrentUserEntity();

        // Maps the entity directly to the ResponseDTO
        return ResponseEntity.ok(userMapper.toResponseDto(currentUser));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> readAll(
            @RequestParam(required = false) String createdBy,
            @AuthenticationPrincipal SecurityUser principal)
            
     {
        List<UserResponseDTO> users = userService.readAll(Optional.ofNullable(createdBy), principal);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') || principal.id == #id")
    public ResponseEntity<UserResponseDTO> readById(@PathVariable String id, @AuthenticationPrincipal SecurityUser principal) {
        UserResponseDTO user = userService.read(id,principal);
        return ResponseEntity.ok(user);
    }



    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') || principal.id == #id")
    public ResponseEntity<UserResponseDTO> update(
            @PathVariable String id,
            @Valid @RequestBody UserUpdateDTO dto,
            @AuthenticationPrincipal SecurityUser principal) {

        UserResponseDTO updated = userService.update(dto, id, principal);
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@Valid @RequestBody UserCreateDTO dto, @AuthenticationPrincipal SecurityUser principal) {
        UserResponseDTO created = userService.create(dto, principal);
        return ResponseEntity.ok(created);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponseDTO> delete(@PathVariable String id) {
        UserResponseDTO deleted = userService.delete(id);
        return ResponseEntity.ok(deleted);
    }


}
