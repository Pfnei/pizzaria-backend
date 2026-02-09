package at.incrustwetrust.pizzeria.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    String saveProfileImage(MultipartFile file, String userId);
    String saveProductImage(MultipartFile file, String productId);
    byte[] loadProfileImage(String filename);
    byte[] loadProductImage(String filename);
    Resource loadProfileImageAsResource(String filename);
    Resource loadProductImageAsResource(String filename);

    void deleteProfileImage(String filename);
    void deleteProductImage(String filename);
}
