package com.Proyecto.Final.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImagenService {
    private final String uploadDirectory = "src/main/resources/static/images/";

    public String buildImage(MultipartFile imagen) throws IOException{
        Date creado = new Date();
        String name = creado.getTime() + "-" + imagen.getOriginalFilename();
        Path uploadPath = Paths.get(uploadDirectory);

        if(!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }try(InputStream inputStream = imagen.getInputStream()){
            Files.copy(inputStream, Paths.get(uploadDirectory, name), StandardCopyOption.REPLACE_EXISTING);
        }
        return name;
    }

    public void deleteImage(String imageName) throws IOException{
        Path path = Paths.get(uploadDirectory + imageName); 
        Files.deleteIfExists(path);
    }

}
