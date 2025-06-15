package me.franciscomolina.back_portal_empleo_mayor50.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;

@Service
public class FileStorageService implements IFileStoraService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public String saveFile(MultipartFile file, Long userId) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío");
        }

        Path userDir = Paths.get(uploadDir, String.valueOf(userId));
        if (!Files.exists(userDir)) {
            Files.createDirectories(userDir);
        }

        String fileName = file.getOriginalFilename();
        Path filePath = userDir.resolve(fileName);
        file.transferTo(filePath.toFile());

        return fileName;
    }

    @Override
    public Resource loadFileAsResource(Long userId, String filename) throws MalformedURLException {
        Path filePath = Paths.get(uploadDir, String.valueOf(userId)).resolve(filename).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new RuntimeException("Archivo no encontrado: " + filename);
        }
    }
}
