package com.ecommerce.Config;

import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
@Getter
@Setter
public class JwtConfig {
@Value("${jwt.expiration}")
    private int expiration;
    @Bean
    public SecretKey secretKey() {
        return Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
    }
}
