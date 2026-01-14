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

    @PostMapping(value = "/profilepicture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> uploadProfileImage(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal SecurityUser principal
    ) {

        var user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String filename  = fileService.saveProfileImage(file, principal.getId());


        user.setProfilePicture(filename );
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }



    //  Download eines Bilds

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<byte[]> getProfilePicture(@AuthenticationPrincipal SecurityUser principal) {


        var user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));


        if (user.getProfilePicture() == null) {
            return ResponseEntity.notFound().build();
        }
        byte[] data = fileService.loadProfileImage(user.getProfilePicture());

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
