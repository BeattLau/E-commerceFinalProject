package com.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication @EnableJpaRepositories("com.ecommerce.User.Repository")
public class eCommerceFinalProjectApplication  {
    public static void main(String[] args) {
        SpringApplication.run(eCommerceFinalProjectApplication.class, args);
    }
}