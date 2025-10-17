package at.incrustwetrust.pizzeria.security;

import at.incrustwetrust.pizzeria.entity.User;
import at.incrustwetrust.pizzeria.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepository users;

    public JpaUserDetailsService(UserRepository users) {
        this.users = users;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = users.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return new SecurityUser(user);
    }
}
