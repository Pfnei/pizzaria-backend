package at.incrustwetrust.pizzeria.auth;

import at.incrustwetrust.pizzeria.entity.User;
import at.incrustwetrust.pizzeria.repository.UserRepository;
import at.incrustwetrust.pizzeria.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authManager, JwtService jwtService,
                          UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ------------------------------------------------------------
    // LOGIN
    // ------------------------------------------------------------
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        String token = jwtService.generateToken((UserDetails) authentication.getPrincipal());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    // ------------------------------------------------------------
    // REGISTRATION
    // ------------------------------------------------------------
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        if (userRepository.findUserByUsername(request.username()).isPresent()) {
            return ResponseEntity.status(409).body("Username already exists");
        }
        if (userRepository.findUserByEmail(request.email()).isPresent()) {
            return ResponseEntity.status(409).body("Email already exists");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setActive(true);
        user.setAdmin(false);

        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }
}
