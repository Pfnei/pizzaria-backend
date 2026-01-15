package at.incrustwetrust.pizzeria.controller;

import at.incrustwetrust.pizzeria.exception.UserNotFoundException;
import at.incrustwetrust.pizzeria.repository.UserRepository;
import at.incrustwetrust.pizzeria.security.SecurityUser;
import at.incrustwetrust.pizzeria.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
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

    @PostMapping(value = "/profilepicture/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN') || principal.id == #userId")
    public ResponseEntity<String> uploadProfileImage(
            @PathVariable String userId,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal SecurityUser principal
    ) {

        // Hier nutzen wir jetzt die userId aus dem Pfad statt principal.getId()
        String filename = fileService.saveProfileImage(file, userId);

        return ResponseEntity.ok().build();
    }



    //  Download eines Bilds

    @GetMapping("/profile/{userId}")
    @PreAuthorize("hasRole('ADMIN') || principal.id == #userId")
    public ResponseEntity<Resource> getProfilePicture(@PathVariable String userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // 1. Wir rufen den Service auf. Dieser gibt IMMER eine Resource zurück
        // (entweder das echte Bild oder den Default-Avatar).
        Resource fileResource = fileService.loadProfileImageAsResource(user.getProfilePicture());

        // 2. Wir müssen den richtigen Header bestimmen
        String filename = user.getProfilePicture();
        MediaType contentType;

        if (filename == null || filename.isEmpty()) {
            // Falls kein Bild da ist, wissen wir, dass der Service "default-avatar.png" liefert
            contentType = MediaType.IMAGE_PNG;
            filename = "default-avatar.png";
        } else {
            // Falls ein Bild da ist, bestimmen wir den Typ dynamisch
            contentType = filename.toLowerCase().endsWith(".png") ?
                    MediaType.IMAGE_PNG : MediaType.IMAGE_JPEG;
        }

        return ResponseEntity.ok()
                .contentType(contentType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                .body(fileResource);
    }


}
