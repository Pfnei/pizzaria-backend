package at.incrustwetrust.pizzeria.service.impl;

import at.incrustwetrust.pizzeria.entity.User;
import at.incrustwetrust.pizzeria.exception.ResourceNotFoundException;
import at.incrustwetrust.pizzeria.exception.UpdateFailedException;
import at.incrustwetrust.pizzeria.exception.UserNotFoundException;
import at.incrustwetrust.pizzeria.repository.UserRepository;
import at.incrustwetrust.pizzeria.service.FileStorageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j

public class FileStorageServiceImpl implements FileStorageService {

    private final UserRepository userRepository;


    private final Path root = Paths.get("uploads/profile-images");

    public FileStorageServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory!", e);
        }
    }

    @Override
    @Transactional
    public String saveProfileImage(MultipartFile file,String userId) {

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        validate(file);

        String oldFilename = user.getProfilePicture();
        String extension = file.getContentType().equals("image/png") ? "png" : "jpg";
        String filename = UUID.randomUUID()  +  "." + extension;

        Path destination = root.resolve(filename).normalize();

        if (!destination.startsWith(root)) {
            throw new SecurityException("Invalid file path");
        }

        // 2. Neue Datei physisch speichern
        try {
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Could not store profile image", e);
        }

        // 3. Datenbank-Update mit Rollback-Schutz für die Datei
        try {
            user.setProfilePicture(filename);
            userRepository.save(user); // Wenn das kracht -> ab in den catch
        } catch (Exception e) {
            deletePhysicalFile(filename); // Neue Datei löschen, da DB-Eintrag fehlgeschlagen
            log.error("Error saving user profile picture in DB", e);
            throw new UpdateFailedException("Could not update user profile in database");
        }

        // 4. Cleanup: Altes Bild erst löschen, wenn ALLES andere geklappt hat
        if (oldFilename != null) {
            deletePhysicalFile(oldFilename);
        }

        return filename;


    }



    @Override
    public byte[] loadProfileImage(String filename) {

            Path file = root.resolve(filename).normalize();
        if (!file.startsWith(root)) {
            throw new SecurityException("Invalid file path");
        }

            try{
            return Files.readAllBytes(file);
        } catch (IOException e) {
            throw new ResourceNotFoundException("Could not read profile image");
        }
    }

    @Override
    public Resource loadProfileImageAsResource(String filename) {
        try {
            if (filename == null || filename.isEmpty()) {
                log.info("Lade Default-Avatar...");
                Resource res = new ClassPathResource("static/images/default-avatar.png");

                if (res.exists()) {
                    log.info("Default-Avatar gefunden! Pfad: {}", res.getURL());
                    return res;
                } else {
                    log.error("Default-Avatar EXISTIERT NICHT im Pfad: src/main/resources/static/images/default-avatar.png");
                    // Letzter Rettungsversuch: Schau mal ob es direkt in static liegt
                    return new ClassPathResource("static/default-avatar.png");
                }
            }

            Path filePath = root.resolve(filename).normalize();
            return new UrlResource(filePath.toUri());

        } catch (Exception e) {
            log.error("Fehler beim Laden der Resource: ", e);
            return new ClassPathResource("static/images/default-avatar.png");
        }
    }

    private Resource getDefaultAvatar() {
        // WICHTIG: Kein führender Slash bei ClassPathResource
        Resource defaultImage = new ClassPathResource("static/images/default-avatar.png");

        log.info("Versuche Default-Avatar zu laden: {}", defaultImage.getDescription());

        if (!defaultImage.exists()) {
            log.error("DATEI NICHT GEFUNDEN! Bitte prüfen: src/main/resources/static/images/default-avatar.png");
            // Fallback: Falls der Ordner "images" vielleicht im Pfad fehlt
            defaultImage = new ClassPathResource("static/default-avatar.png");
        }

        if (!defaultImage.exists()) {
            throw new ResourceNotFoundException("Absolut kein Standard-Bild gefunden.");
        }

        return defaultImage;
    }
    private void validate(MultipartFile file) {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        if (file.getSize() > 2_000_000) {
            throw new IllegalArgumentException("File too large");
        }

        if (!List.of("image/jpeg", "image/png").contains(file.getContentType())) {
            throw new IllegalArgumentException("Unsupported file type");
        }
    }

    private void deletePhysicalFile(String filename) {
        Path file = root.resolve(filename).normalize();
        try {
            Files.deleteIfExists(file);
        } catch (IOException e) {
            log.warn("Could not delete profile image: {}", file, e);
        }
    }

}
