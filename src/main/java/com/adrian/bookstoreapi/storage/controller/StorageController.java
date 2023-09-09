package com.adrian.bookstoreapi.storage.controller;

import com.adrian.bookstoreapi.storage.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/v1/media")
public class StorageController {

    @Autowired
    @Qualifier("FileSystemStorageService")
    private StorageService storageService;


    @PostMapping("/upload")
    public ResponseEntity<?> upload(
            // form-data key must be named as file
            @RequestParam(name = "file") MultipartFile file
    ) {
        storageService.store(file);

        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }
}
