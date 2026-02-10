package at.incrustwetrust.pizzeria.service;

import at.incrustwetrust.pizzeria.dto.user.*;
import at.incrustwetrust.pizzeria.entity.User;
import at.incrustwetrust.pizzeria.exception.UnauthorizedActionException;
import at.incrustwetrust.pizzeria.exception.UserAlreadyExistsException;
import at.incrustwetrust.pizzeria.exception.UserNotFoundException;
import at.incrustwetrust.pizzeria.mapper.UserMapper;
import at.incrustwetrust.pizzeria.repository.UserRepository;
import at.incrustwetrust.pizzeria.security.SecurityUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    UserMapper mapper;

    @Mock
    FileStorageService fileService;

    @InjectMocks
    UserService userService;

    private SecurityUser principal(String id, boolean admin) {
        User u = new User();
        u.setUserId(id);
        u.setAdmin(admin);
        u.setActive(true);
        u.setEmail("p@x");
        u.setUsername("puser");
        u.setPassword("pw");
        return new SecurityUser(u);
    }

    @Test
    void create_success_setsCreator_andEncodesPassword_andReturnsDto() {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setEmail("mail@ex.com");
        dto.setUsername("john");
        dto.setPassword("plain");

        when(userRepository.findUserByEmail("mail@ex.com")).thenReturn(Optional.empty());
        when(userRepository.findUserByUsername("john")).thenReturn(Optional.empty());

        User creator = new User();
        creator.setUserId("creator");
        when(userRepository.findById("creator")).thenReturn(Optional.of(creator));

        User toSave = new User();
        when(mapper.toEntity(eq(dto), isNull())).thenReturn(toSave);
        when(passwordEncoder.encode("plain")).thenReturn("ENC");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        when(userRepository.save(userCaptor.capture())).thenAnswer(inv -> inv.getArgument(0));

        UserResponseDTO mapped = new UserResponseDTO();
        when(mapper.toResponseDto(any(User.class))).thenReturn(mapped);

        SecurityUser principal = principal("creator", true);
        UserResponseDTO result = userService.create(dto, principal);

        User saved = userCaptor.getValue();
        assertThat(saved.getCreatedBy()).isSameAs(creator);
        assertThat(saved.getPassword()).isEqualTo("ENC");
        assertThat(result).isSameAs(mapped);
    }

    @Test
    void create_throws_onDuplicateEmail() {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setEmail("dup@ex.com");
        dto.setUsername("x");
        when(userRepository.findUserByEmail("dup@ex.com")).thenReturn(Optional.of(new User()));
        assertThatThrownBy(() -> userService.create(dto, principal("c", true)))
                .isInstanceOf(UserAlreadyExistsException.class);
    }

    @Test
    void read_success_returnsDto() {
        User u = new User();
        when(userRepository.findById("id1")).thenReturn(Optional.of(u));
        UserResponseDTO dto = new UserResponseDTO();
        when(mapper.toResponseDto(u)).thenReturn(dto);
        UserResponseDTO res = userService.read("id1", principal("p", true));
        assertThat(res).isSameAs(dto);
    }

    @Test
    void read_throws_whenNotFound() {
        when(userRepository.findById("missing")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.read("missing", principal("p", true)))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void readAll_mapsList() {
        when(userRepository.findAll()).thenReturn(List.of(new User(), new User()));
        List<UserResponseDTO> list = List.of(new UserResponseDTO(), new UserResponseDTO());
        when(mapper.toResponseDtoList(anyList())).thenReturn(list);
        List<UserResponseDTO> res = userService.readAll();
        assertThat(res).isSameAs(list);
    }

    @Test
    void update_asSelf_protectsAdminAndActive_andEncodesPassword_whenProvided() {
        // existing with admin/active true
        User existing = new User();
        existing.setUserId("u1");
        existing.setAdmin(true);
        existing.setActive(true);
        when(userRepository.findById("u1")).thenReturn(Optional.of(existing));

        // no duplicates
        when(userRepository.findByEmailAndUserIdNot(any(), any())).thenReturn(Optional.empty());
        when(userRepository.findUserByUsernameAndUserIdNot(any(), any())).thenReturn(Optional.empty());

        // Simulate mapper trying to change flags
        doAnswer(inv -> {
            UserUpdateDTO d = inv.getArgument(0);
            User ent = inv.getArgument(1);
            ent.setAdmin(false); // should be reverted for non-admin
            ent.setActive(false); // should be reverted for non-admin
            ent.setEmail(d.getEmail());
            return null;
        }).when(mapper).updateEntity(any(UserUpdateDTO.class), eq(existing));

        // current principal user
        User currentUser = new User();
        currentUser.setUserId("u1");
        when(userRepository.findById("u1")).thenReturn(Optional.of(existing))
                .thenReturn(Optional.of(currentUser)); // second call for lastUpdatedBy

        when(passwordEncoder.encode("newPw")).thenReturn("ENC-PW");

        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
        when(mapper.toResponseDto(any(User.class))).thenReturn(new UserResponseDTO());

        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setPassword("newPw");
        dto.setEmail("new@x.com");

        UserResponseDTO res = userService.update(dto, "u1", principal("u1", false));

        assertThat(existing.isAdmin()).isTrue();
        assertThat(existing.isActive()).isTrue();
        assertThat(existing.getPassword()).isEqualTo("ENC-PW");
        assertThat(res).isNotNull();
    }

    @Test
    void update_throws_whenUnauthorized_notAdminNorSelf() {
        User existing = new User();
        existing.setUserId("target");
        when(userRepository.findById("target")).thenReturn(Optional.of(existing));
        assertThatThrownBy(() -> userService.update(new UserUpdateDTO(), "target", principal("other", false)))
                .isInstanceOf(UnauthorizedActionException.class);
    }

    @Test
    void update_throws_onDuplicateEmailOrUsername() {
        User existing = new User();
        existing.setUserId("u1");
        when(userRepository.findById("u1")).thenReturn(Optional.of(existing));
        when(userRepository.findByEmailAndUserIdNot(any(), any()))
                .thenReturn(Optional.of(new User()));
        assertThatThrownBy(() -> userService.update(new UserUpdateDTO(), "u1", principal("u1", true)))
                .isInstanceOf(UserAlreadyExistsException.class);
    }

    @Test
    void delete_deletesProfileImage_whenPresent_andDeletesUser() {
        User existing = new User();
        existing.setUserId("u1");
        existing.setProfilePicture("pic.png");
        when(userRepository.findById("u1")).thenReturn(Optional.of(existing));
        when(mapper.toResponseDto(existing)).thenReturn(new UserResponseDTO());

        UserResponseDTO res = userService.delete("u1");

        verify(fileService).deleteProfileImage("pic.png");
        verify(userRepository).delete(existing);
        assertThat(res).isNotNull();
    }

    @Test
    void delete_throws_whenUserNotFound() {
        when(userRepository.findById("x")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.delete("x")).isInstanceOf(UserNotFoundException.class);
    }
}
