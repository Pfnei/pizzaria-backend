package at.incrustwetrust.pizzeria.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    String saveFile(MultipartFile file, String userId);
    byte[] loadFile(String filename);
}
