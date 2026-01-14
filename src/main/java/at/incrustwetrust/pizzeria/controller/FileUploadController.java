package at.incrustwetrust.pizzeria.controller;

import at.incrustwetrust.pizzeria.repository.UserRepository;
import at.incrustwetrust.pizzeria.security.SecurityUser;
import at.incrustwetrust.pizzeria.service.FileStorageService;
import lombok.RequiredArgsConstructor;
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

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setProfilePicture(filename);
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }



    //  Download eines Bilds

    @GetMapping("/profile/{userId}")
    @PreAuthorize("hasRole('ADMIN') || principal.id == #userId")
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable String userId,@AuthenticationPrincipal SecurityUser principal) {


        var user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));


        if (user.getProfilePicture() == null) {
            return ResponseEntity.notFound().build();
        }
        byte[] data = fileService.loadProfileImage(userId);

        MediaType contentType = user.getProfilePicture().endsWith(".png") ?
                MediaType.IMAGE_PNG : MediaType.IMAGE_JPEG;

        return ResponseEntity.ok()
                .contentType(contentType)
                .body(data);


      //  .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
             //   .contentType(MediaType.IMAGE_JPEG)
            //    .body(data);
    }



}
