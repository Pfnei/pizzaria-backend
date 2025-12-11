package at.incrustwetrust.pizzeria.security;


import org.springframework.stereotype.Component;

@Component("fileSecurity")
public class FileSecurity {

    public boolean canAccess(String filename, SecurityUser user) {
        if (filename == null || user == null) return false;
        return filename.startsWith("profile_" + user.getId());
    }
}
