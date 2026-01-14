package at.incrustwetrust.pizzeria.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    String saveProfileImage(MultipartFile file, String userId);
    byte[] loadProfileImage(String filename);
}
