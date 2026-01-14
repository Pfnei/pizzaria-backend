package at.incrustwetrust.pizzeria.service.impl;

import at.incrustwetrust.pizzeria.entity.User;
import at.incrustwetrust.pizzeria.repository.UserRepository;
import at.incrustwetrust.pizzeria.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
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
    public String saveProfileImage(MultipartFile file,String userId) {

        validate(file);



        String extension = file.getContentType().equals("image/png") ? "png" : "jpg";
        String filename = UUID.randomUUID() + "." + extension;

        Path destination = root.resolve(filename).normalize();

        if (!destination.startsWith(root)) {
            throw new SecurityException("Invalid file path");
        }

        User user = userRepository.findById(userId).orElseThrow(()
                -> new RuntimeException("User not found"));
        // altes Bild löschen
        if (user.getProfilePicture() != null) {
            Path oldFile = root.resolve(user.getProfilePicture()).normalize();
            try {
                Files.deleteIfExists(oldFile);
            } catch (IOException e) {
                log.warn("Could not delete old profile image: {}", oldFile, e);
            }
        }


        try {
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
            log.info("Profile image stored: {}", destination);
            return filename;
        } catch (IOException e) {
            throw new RuntimeException("Could not store profile image", e);
        }
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
            throw new RuntimeException("Could not read profile image", e);
        }
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
}
