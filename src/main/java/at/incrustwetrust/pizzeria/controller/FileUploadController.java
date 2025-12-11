package at.incrustwetrust.pizzeria.controller;

import at.incrustwetrust.pizzeria.repository.UserRepository;
import at.incrustwetrust.pizzeria.security.SecurityUser;
import at.incrustwetrust.pizzeria.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileStorageService fileService;
    private final UserRepository userRepository;

    // Upload eines Profilbilds

    @PostMapping(value = "/profilepicture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> uploadProfileImage(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal SecurityUser principal
    ) {

        var user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String saved = fileService.saveFile(file, user.getUserId());
        user.setProfilePicture(saved);
        userRepository.save(user);

        return ResponseEntity.ok(saved);
    }



    //  Download eines Bilds

    @GetMapping("/{filename}")
    @PreAuthorize("hasRole('ADMIN') or @fileSecurity.canAccess(#filename, principal)")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String filename) {

        byte[] data = fileService.loadFile(filename);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(data);
    }


}
