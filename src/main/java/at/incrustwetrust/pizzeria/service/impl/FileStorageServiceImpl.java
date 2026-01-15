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
            Path filePath;

            // Wenn kein Dateiname da ist, nimm den Default-Pfad
            if (filename == null || filename.isEmpty()) {
                return getDefaultAvatar();
            }

            filePath = root.resolve(filename).normalize();

            // Sicherheitscheck
            if (!filePath.startsWith(root)) {
                throw new SecurityException("Invalid file path");
            }

            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                // Falls der Dateiname in der DB stand, aber die Datei gelöscht wurde
                log.warn("File {} not found, serving default avatar", filename);
                return new ClassPathResource("static/images/default-avatar.png");
            }
        } catch (Exception e) {
            log.error("Error loading image, serving default", e);
            return new ClassPathResource("static/images/default-avatar.png");
        }
    }

    private Resource getDefaultAvatar() {
        Resource defaultImage = new ClassPathResource("static/images/default-avatar.png");
        if (!defaultImage.exists()) {
            // Wenn selbst das fehlt, ist beim Deployment was schiefgelaufen
            log.error("KRITISCH: Default-Avatar unter resources/static/images/default-avatar.png fehlt!");
            // Hier könntest du eine Fallback-Exception werfen oder eine leere Resource
            throw new ResourceNotFoundException("Systemressource fehlt");
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
