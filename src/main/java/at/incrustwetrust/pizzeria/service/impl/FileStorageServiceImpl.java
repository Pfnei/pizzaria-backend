package at.incrustwetrust.pizzeria.service.impl;


import at.incrustwetrust.pizzeria.exception.ResourceNotFoundException;
import at.incrustwetrust.pizzeria.exception.UpdateFailedException;
import at.incrustwetrust.pizzeria.exception.UserNotFoundException;
import at.incrustwetrust.pizzeria.repository.ProductRepository;
import at.incrustwetrust.pizzeria.repository.UserRepository;
import at.incrustwetrust.pizzeria.service.FileStorageService;
import jakarta.transaction.Transactional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;

import java.nio.file.*;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j

public class FileStorageServiceImpl implements FileStorageService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;


    private final Path profileRoot = Paths.get("uploads/profile-images");
    private final Path productRoot = Paths.get("uploads/product-images");

    public FileStorageServiceImpl(UserRepository userRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        try {
            Files.createDirectories(profileRoot);
            Files.createDirectories(productRoot);
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

        Path destination = profileRoot.resolve(filename).normalize();

        if (!destination.startsWith(profileRoot)) {
            throw new SecurityException("Invalid file path");
        }

        // 2. Save new file physically
        try {
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Could not store profile image", e);
        }

        // 3. Database update with rollback protection for the file
        try {
            user.setProfilePicture(filename);
            userRepository.save(user); // If this crashes -> go to catch
        } catch (Exception e) {
            deletePhysicalFile(profileRoot, filename); // Delete new file because DB entry failed
            log.error("Error saving user profile picture in DB", e);
            throw new UpdateFailedException("Could not update user profile in database");
        }

        // 4. Cleanup: Delete old image only if EVERYTHING else worked
        if (oldFilename != null) {
            deletePhysicalFile(profileRoot, oldFilename);
        }

        return filename;
    }

    @Override
    @Transactional
    public String saveProductImage(MultipartFile file, String productId) {
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        validate(file);

        String oldFilename = product.getProductPicture();
        String extension = file.getContentType().equals("image/png") ? "png" : "jpg";
        String filename = UUID.randomUUID() + "." + extension;

        Path destination = productRoot.resolve(filename).normalize();

        if (!destination.startsWith(productRoot)) {
            throw new SecurityException("Invalid file path");
        }

        try {
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Could not store product image", e);
        }

        try {
            product.setProductPicture(filename);
            productRepository.save(product);
        } catch (Exception e) {
            deletePhysicalFile(productRoot, filename);
            log.error("Error saving product picture in DB", e);
            throw new UpdateFailedException("Could not update product picture in database");
        }

        if (oldFilename != null) {
            deletePhysicalFile(productRoot, oldFilename);
        }

        return filename;
    }



    @Override
    public byte[] loadProfileImage(String filename) {
        return loadFile(profileRoot, filename);
    }

    @Override
    public byte[] loadProductImage(String filename) {
        return loadFile(productRoot, filename);
    }

    private byte[] loadFile(Path root, String filename) {
        Path file = root.resolve(filename).normalize();
        if (!file.startsWith(root)) {
            throw new SecurityException("Invalid file path");
        }

        try {
            return Files.readAllBytes(file);
        } catch (IOException e) {
            throw new ResourceNotFoundException("Could not read file");
        }
    }

    @Override
    public Resource loadProfileImageAsResource(String filename) {
        return loadResource(profileRoot, filename, "static/images/default-avatar.png");
    }

    @Override
    public Resource loadProductImageAsResource(String filename) {
        // Here you could add a default-product-image.png if desired
        return loadResource(productRoot, filename, "static/images/default-product-avatar.jpg");
    }

    private Resource loadResource(Path root, String filename, String defaultPath) {
        try {
            if (filename == null || filename.isEmpty()) {
                log.info("Loading default resource...");
                Resource res = new ClassPathResource(defaultPath);

                if (res.exists()) {
                    return res;
                } else {
                    log.error("Default resource DOES NOT EXIST in path: {}", defaultPath);
                    return new ClassPathResource("static/images/default-avatar.png");
                }
            }

            Path filePath = root.resolve(filename).normalize();
            return new UrlResource(filePath.toUri());

        } catch (Exception e) {
            log.error("Error loading resource: ", e);
            return new ClassPathResource("static/images/default-avatar.png");
        }
    }

    @Override
    public void deleteProfileImage(String filename) {
        if (filename != null) {
            deletePhysicalFile(profileRoot, filename);
        }
    }

    @Override
    public void deleteProductImage(String filename) {
        if (filename != null) {
            deletePhysicalFile(productRoot, filename);
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

    private void deletePhysicalFile(Path root, String filename) {
        Path file = root.resolve(filename).normalize();
        try {
            Files.deleteIfExists(file);
        } catch (IOException e) {
            log.warn("Could not delete file: {}", file, e);
        }
    }

}
