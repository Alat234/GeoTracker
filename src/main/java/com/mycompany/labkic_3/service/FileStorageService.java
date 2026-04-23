package com.mycompany.labkic_3.service;

import com.mycompany.labkic_3.config.StorageProperties;
import com.mycompany.labkic_3.exception.TrackStorageException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

@Service
public class FileStorageService {
    private final Path rootLocation;

    public FileStorageService(StorageProperties storageProperties) {
        this.rootLocation = Paths.get(storageProperties.getLocation());
    }

    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new TrackStorageException("Failed to initialize storage location", e);
        }
    }

    public String store(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
                throw new TrackStorageException("Cannot store empty file", null);
            }
            try (InputStream saveStream = file.getInputStream()) {
                Files.copy(saveStream, rootLocation.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
            }
            return filename;
        } catch (IOException e) {
            throw new TrackStorageException("Failed to store file: " + filename, e);
        }
    }
}
