package me.franciscomolina.back_portal_empleo_mayor50.controllers;

import me.franciscomolina.back_portal_empleo_mayor50.security.UserEntityPrincipal;
import me.franciscomolina.back_portal_empleo_mayor50.security.jwt.JwtProvider;
import me.franciscomolina.back_portal_empleo_mayor50.services.IFileStoraService;
import me.franciscomolina.back_portal_empleo_mayor50.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private IFileStoraService fileStorageService;

    @Autowired
    private JwtProvider jwtProvider;  // Asegúrate de tener esta referencia

    /*@PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        // Extraer el token del encabezado
        String token = SecurityUtils.extractAuthTokenFromRequest(request);

        if (token == null) {
            return ResponseEntity.status(401).body("Token de autenticación no proporcionado");
        }

        try {
            // Extraer el userId del token
            Authentication authentication = jwtProvider.getAuthentication(request);
            Long userId = ((UserEntityPrincipal) authentication.getPrincipal()).getId();

            // Ahora puedes usar el userId sin tener que pasarlo explícitamente en la solicitud
            String filePath = fileStorageService.saveFile(file, userId);
            return ResponseEntity.ok("Archivo subido exitosamente: " + filePath);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al subir el archivo: " + e.getMessage());
        }
    }*/
}
