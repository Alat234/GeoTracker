package com.mycompany.labkic_3;

import com.mycompany.labkic_3.Storages.FileStorage;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(FileStorage.class)
public class LabKic3Application {

    public static void main(String[] args) {
        SpringApplication.run(LabKic3Application.class, args);
    }
    @Bean
    CommandLineRunner init(FileStorage storage) {
        return (args) -> {
            storage.init();
        };
    }

}
