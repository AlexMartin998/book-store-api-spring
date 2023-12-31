package com.adrian.bookstoreapi.storage.controller;

import com.adrian.bookstoreapi.common.constants.RoleConstants;
import com.adrian.bookstoreapi.storage.dto.FileStoredResponseDto;
import com.adrian.bookstoreapi.storage.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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
    @Secured(RoleConstants.ADMIN)
    public ResponseEntity<FileStoredResponseDto> upload(
            // form-data key must be named as file
            @RequestParam(name = "file") MultipartFile file
    ) {
        return new ResponseEntity<>(storageService.store(file), HttpStatus.CREATED);
    }

}
