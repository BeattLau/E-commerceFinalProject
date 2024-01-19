package eCommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.security.Security;


@SpringBootApplication
public class eCommerceFinalProjectApplication  {
    public static void main(String[] args) {
        SpringApplication.run(eCommerceFinalProjectApplication.class, args);
    }
}


