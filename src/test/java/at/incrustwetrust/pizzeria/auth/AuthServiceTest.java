package at.incrustwetrust.pizzeria.auth;

import at.incrustwetrust.pizzeria.dto.user.UserResponseLightDTO;
import at.incrustwetrust.pizzeria.entity.User;
import at.incrustwetrust.pizzeria.mapper.UserMapper;
import at.incrustwetrust.pizzeria.repository.UserRepository;
import at.incrustwetrust.pizzeria.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    JwtService jwtService;

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    UserMapper mapper;

    @InjectMocks
    AuthService authService;

    @Test
    void login_returnsTokenAndUser_whenCredentialsAreValid() {
        LoginRequest request = new LoginRequest("test@mail.com", "pw");

        UserDetails userDetails =
                org.springframework.security.core.userdetails.User
                        .withUsername("test@mail.com")
                        .password("encoded")
                        .authorities("USER")
                        .build();

        Authentication authentication = mock(Authentication.class);
        User userEntity = new User();
        UserResponseLightDTO dto = new UserResponseLightDTO(1L, "test@mail.com");

        when(authenticationManager.authenticate(any()))
                .thenReturn(authentication);

        when(authentication.getPrincipal())
                .thenReturn(userDetails);

        when(jwtService.generateToken(userDetails))
                .thenReturn("jwt-token");

        when(userRepository.findUserByEmail("test@mail.com"))
                .thenReturn(Optional.of(userEntity));

        when(mapper.toResponseLightDto(userEntity))
                .thenReturn(dto);

        AuthResponse response = authService.login(request);

        assertThat(response.accessToken()).isEqualTo("jwt-token");
        assertThat(response.user()).isEqualTo(dto);
    }
}
