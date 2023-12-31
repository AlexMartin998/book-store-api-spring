package com.adrian.bookstoreapi.storage.service;

import com.adrian.bookstoreapi.common.exceptions.ResourceNotFoundException;
import com.adrian.bookstoreapi.common.exceptions.StorageException;
import com.adrian.bookstoreapi.storage.dto.FileStoredResponseDto;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;


@Service("FileSystemStorageService") // to be used with @Qualifier
//@Service
public class FileSystemStorageService implements StorageService {

    @Value("${storage.location}")
    private String storageLocation; // dirname that will store files


    // // como es 1 @Bean (@Service) se genera 1 sola instancia (singleton) cuando arranca Spring, asi q init() se ejecuta 1 sola vex
    // cuando arranca Spring x primera vez y NO con c/inyeccion, ya q se inyecta la misma instancia | Todos los @Bean gestionados x spring sigen el patron Singleton
    @Override
    @PostConstruct
    public void init() {
        // create directory after constructor generation only if it does not exist
        Path storagePath = Paths.get(storageLocation);
        if (!Files.exists(storagePath)) {
            try {
                Files.createDirectory(storagePath);
            } catch (IOException ex) {
                throw new StorageException("Failed to create file storage directory");
            }
        }
    }

    @Override
    public FileStoredResponseDto store(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String filename = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(originalFilename);

        if (file.isEmpty()) throw new StorageException("Invalid empty file");

        try {
            InputStream inputStream = file.getInputStream();
            Files.copy(inputStream, Paths.get(storageLocation).resolve(filename), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new StorageException("File could not be stored");
        }

        FileStoredResponseDto fileStoredResponseDto = new FileStoredResponseDto();
        fileStoredResponseDto.setFilename(filename);

        return fileStoredResponseDto;
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path path = Paths.get(storageLocation).resolve(filename);
            Resource resource = new UrlResource(path.toUri());

            if (resource.exists() || resource.isReadable()) return resource;
            else throw new RuntimeException();
        } catch (Exception ex) {
            throw new ResourceNotFoundException("File", "filename", filename);
        }
    }

    @Override
    public void delete(String filename) {
        Path path = Paths.get(storageLocation).resolve(filename);

        try {
            FileSystemUtils.deleteRecursively(path);
        } catch (IOException e) {
            // let it happen
        }
    }
}
