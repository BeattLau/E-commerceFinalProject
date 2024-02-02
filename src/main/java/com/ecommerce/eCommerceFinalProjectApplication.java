package com.ecommerce;

import com.ecommerce.Config.Documentation.LoggingConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class eCommerceFinalProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(eCommerceFinalProjectApplication.class, args);
    }
}