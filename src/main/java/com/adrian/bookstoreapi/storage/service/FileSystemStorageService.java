package com.adrian.bookstoreapi.storage.service;

import com.adrian.bookstoreapi.common.exceptions.ResourceNotFoundException;
import com.adrian.bookstoreapi.common.exceptions.StorageException;
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
    public String store(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String filename = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(originalFilename);

        if (file.isEmpty()) throw new StorageException("Invalid empty file");

        try {
            InputStream inputStream = file.getInputStream();
            Files.copy(inputStream, Paths.get(storageLocation).resolve(filename), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new StorageException("File could not be stored");
        }

        return filename;
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
