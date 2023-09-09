package com.adrian.bookstoreapi.storage.service;

import com.adrian.bookstoreapi.storage.dto.FileStoredResponseDto;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;


public interface StorageService {

    void init();

    FileStoredResponseDto store(MultipartFile file);

    Resource loadAsResource(String filename);

    void delete(String filename);

}
