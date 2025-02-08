package me.franciscomolina.back_portal_empleo_mayor50.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageService implements IFileStoraService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public String saveFile(MultipartFile file, Long userId) throws IOException {
        if (file.isEmpty()){
            throw new IllegalArgumentException("El archivo está vacío");
        }
        Path uploadPath = Paths.get(uploadDir, String.valueOf(userId));
        if(!Files.exists(uploadPath)){
            try {
                Files.createDirectories(uploadPath);
            } catch (Exception e) {
                throw new IllegalArgumentException("No se pudo crear el directorio para subir el archivo");
            }
        }
        //guardamos el fichero
        String fileName = file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        file.transferTo(filePath.toFile());

        return filePath.toString();
    }
}
