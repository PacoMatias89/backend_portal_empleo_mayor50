package me.franciscomolina.back_portal_empleo_mayor50.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

public interface IFileStoraService {

    public String saveFile(MultipartFile file, Long userId)throws IOException;

    Resource loadFileAsResource(Long userId, String filename) throws MalformedURLException;
}
