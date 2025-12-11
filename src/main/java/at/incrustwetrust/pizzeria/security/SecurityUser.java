package at.incrustwetrust.pizzeria.security;

import at.incrustwetrust.pizzeria.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class SecurityUser implements UserDetails {

    private final User user;

    public SecurityUser(User user) {
        this.user = user;
    }

    // --- Rollen / Authorities ---
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.isAdmin() ? "ROLE_ADMIN" : "ROLE_USER"));
    }

    // --- Login-Daten (Security-Sicht) ---
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Security-"Username" = Login-Identität
     * Bei uns email
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    // --- Account-Status ---
    @Override
    public boolean isAccountNonExpired() {
        return user.isActive();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.isActive();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isActive();
    }

    // --- Convenience-Methoden für JWT & App-Logik ---

    /**
     * E-Mail, wie oben in getUsername(), aber explizit benannt.
     */
    public String getEmail() {
        return user.getEmail();
    }

    /**
     * Das ist dein "Benutzername" aus der Entity (für UI/Anzeige).
     */
    public String getDisplayUsername() {
        return user.getUsername(); // Feld in deiner User-Entity
    }

    public boolean isAdmin() {
        return user.isAdmin();
    }

    public String getId() {
        return user.getUserId();
    }

    public boolean hasRole(String role) {
        return getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + role));
    }

}
