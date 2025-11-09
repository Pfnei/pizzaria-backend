package at.incrustwetrust.pizzeria.auth;

import at.incrustwetrust.pizzeria.dto.user.UserResponseLightDTO;
import at.incrustwetrust.pizzeria.entity.User;
import at.incrustwetrust.pizzeria.mapper.UserMapper;
import at.incrustwetrust.pizzeria.repository.UserRepository;
import at.incrustwetrust.pizzeria.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;

    // ------------------------------------------------------------
    // LOGIN
    // ------------------------------------------------------------
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        UserDetails principal = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(principal);

        User userEntity = userRepository
                .findUserByUsername(principal.getUsername())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found: " + principal.getUsername()
                ));

        UserResponseLightDTO userDto = mapper.toResponseLightDto(userEntity);
        return ResponseEntity.ok(new AuthResponse(token, userDto));
    }

    // ------------------------------------------------------------
    // REGISTRATION
    // ------------------------------------------------------------
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        userRepository.findUserByUsername(request.username()).ifPresent(u -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        });
        userRepository.findUserByEmail(request.email()).ifPresent(u -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        });

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setIsActive(true);
        user.setIsAdmin(false);

        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }
}
