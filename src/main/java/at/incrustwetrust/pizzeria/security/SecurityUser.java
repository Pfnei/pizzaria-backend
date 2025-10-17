package at.incrustwetrust.pizzeria.security;

import at.incrustwetrust.pizzeria.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class SecurityUser implements UserDetails {
    private final User user;

    public SecurityUser(User user) { this.user = user; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.isAdmin() ? "ROLE_ADMIN" : "ROLE_USER"));
    }

    @Override public String getPassword() { return user.getPassword(); }
    @Override public String getUsername() { return user.getUsername(); }
    @Override public boolean isAccountNonExpired() { return user.isActive(); }
    @Override public boolean isAccountNonLocked() { return user.isActive(); }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return user.isActive(); }

    public String getEmail() { return user.getEmail(); }
}
