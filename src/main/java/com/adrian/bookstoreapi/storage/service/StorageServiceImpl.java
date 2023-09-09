package com.adrian.bookstoreapi.storage.service;

import com.adrian.bookstoreapi.common.exceptions.BadRequestException;
import com.adrian.bookstoreapi.common.exceptions.StorageException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;


@Service("StorageServiceImpl") // to be used with @Qualifier
public class StorageServiceImpl implements StorageService {

    @Value("${storage.location}")
    private String storageLocation; // dirname that will store files


    @Override
    @PostConstruct
    public void init() {
        // create directory after constructor generation
        try {
            Files.createDirectory(Paths.get(storageLocation));
        } catch (IOException ex) {
            throw new StorageException("Failed to create file storage directory");
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
        return null;
    }

    @Override
    public void delete(String filename) {

    }
}
