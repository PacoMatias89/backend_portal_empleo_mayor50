package me.franciscomolina.back_portal_empleo_mayor50.controllers;

import me.franciscomolina.back_portal_empleo_mayor50.entities.UserEntity;
import me.franciscomolina.back_portal_empleo_mayor50.repositories.UserRepository;
import me.franciscomolina.back_portal_empleo_mayor50.security.UserEntityPrincipal;
import me.franciscomolina.back_portal_empleo_mayor50.security.jwt.JwtProvider;
import me.franciscomolina.back_portal_empleo_mayor50.services.IFileStoraService;
import me.franciscomolina.back_portal_empleo_mayor50.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private IFileStoraService fileStorageService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProvider jwtProvider;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        String token = SecurityUtils.extractAuthTokenFromRequest(request);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token de autenticación no proporcionado");
        }

        try {
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body("Archivo no recibido o vacío");
            }

            Authentication authentication = jwtProvider.getAuthentication(token);
            Long userId = ((UserEntityPrincipal) authentication.getPrincipal()).getId();

            String savedFileName = fileStorageService.saveFile(file, userId);

            return ResponseEntity.ok().body("Archivo subido exitosamente. Nombre: " + savedFileName);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error al subir el archivo: " + e.getMessage());
        }
    }

    @GetMapping("/download/{userId}")
    public ResponseEntity<?> downloadUserCv(
            @PathVariable Long userId,
            Authentication authentication
    ) {
        String email = authentication.getName();
        UserEntity currentUser =userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        boolean isAdminOrCompany = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_COMPANY"));

        if (!isAdminOrCompany && !currentUser.getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Path userDir = Paths.get(uploadDir, userId.toString());
        if (!Files.exists(userDir) || !Files.isDirectory(userDir)) {
            return ResponseEntity.notFound().build();
        }

        try {
            return Files.list(userDir)
                    .filter(path -> Files.isRegularFile(path) && path.toString().toLowerCase().endsWith(".pdf"))
                    .findFirst()
                    .map(path -> {
                        try {
                            Resource resource = new UrlResource(path.toUri());
                            return ResponseEntity.ok()
                                    .contentType(MediaType.APPLICATION_PDF)
                                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                                    .body(resource);
                        } catch (IOException e) {
                            return ResponseEntity.internalServerError().build();
                        }
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/exists/{userId}")
    public ResponseEntity<Void> checkIfUserCvExists(@PathVariable Long userId, Authentication authentication) {
        UserEntity currentUser =userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        boolean isAdminOrCompany = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_COMPANY"));

        if (!isAdminOrCompany && !currentUser.getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Path userDir = Paths.get(uploadDir, userId.toString());

        try {
            boolean exists = Files.exists(userDir) && Files.list(userDir)
                    .anyMatch(path -> Files.isRegularFile(path) && path.toString().toLowerCase().endsWith(".pdf"));

            return exists ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



}
