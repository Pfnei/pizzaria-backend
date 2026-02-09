package at.incrustwetrust.pizzeria.utils;

import at.incrustwetrust.pizzeria.entity.User;
import at.incrustwetrust.pizzeria.exception.ResourceNotFoundException;
import at.incrustwetrust.pizzeria.security.SecurityUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserService {
	
	public User getCurrentUserEntity() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !(auth.getPrincipal() instanceof SecurityUser secUser)) {
			throw new ResourceNotFoundException("Kein eingeloggter User im SecurityContext");
		}
		return secUser.getUserEntity();
	}
}