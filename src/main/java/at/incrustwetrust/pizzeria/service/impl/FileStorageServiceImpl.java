package at.incrustwetrust.pizzeria.service.impl;

import at.incrustwetrust.pizzeria.service.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Service
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {

    private final Path root = Paths.get("uploads");

    public FileStorageServiceImpl() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory!", e);
        }
    }

    @Override
    public String saveFile(MultipartFile file, String userId) {
        try {
            String cleanName = StringUtils.cleanPath(file.getOriginalFilename());
            String filename = userId + "_" + System.currentTimeMillis() + "_" + cleanName;

            Path destination = root.resolve(filename);
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

            log.info("File saved: {}", destination);

            return filename;

        } catch (IOException e) {
            throw new RuntimeException("Could not store file!", e);
        }
    }

    @Override
    public byte[] loadFile(String filename) {
        try {
            Path file = root.resolve(filename);
            return Files.readAllBytes(file);
        } catch (IOException e) {
            throw new RuntimeException("Could not read file!", e);
        }
    }
}
