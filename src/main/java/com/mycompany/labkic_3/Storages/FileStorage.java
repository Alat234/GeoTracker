package com.mycompany.labkic_3.Storages;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@ConfigurationProperties("storage")
public class FileStorage {
    private String location = "upload-dir";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    public void init(){
        try {
            Files.createDirectories(Path.of(location));
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to initialize storage location: " + location, e);
        }

    }
}

