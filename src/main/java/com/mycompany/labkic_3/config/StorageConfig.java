package com.mycompany.labkic_3.config;

import com.mycompany.labkic_3.service.FileStorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {

    @Bean
    CommandLineRunner initStorage(FileStorageService fileStorageService) {
        return args -> fileStorageService.init();
    }
}
