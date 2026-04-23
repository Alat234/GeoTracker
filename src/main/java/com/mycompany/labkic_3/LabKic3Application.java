package com.mycompany.labkic_3;

import com.mycompany.labkic_3.config.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class LabKic3Application {

    public static void main(String[] args) {
        SpringApplication.run(LabKic3Application.class, args);
    }
}
