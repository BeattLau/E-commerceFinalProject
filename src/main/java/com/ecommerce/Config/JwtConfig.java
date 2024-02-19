package com.ecommerce.Config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component @Getter @Setter
@ConfigurationProperties(prefix ="jwt")
public class JwtConfig {

    private String secret;
    private int expiration;
}