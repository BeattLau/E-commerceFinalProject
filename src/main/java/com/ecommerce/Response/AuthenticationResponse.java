package com.ecommerce.Response;

import lombok.*;

@Data @Builder
@AllArgsConstructor @NoArgsConstructor @Getter
public class AuthenticationResponse {
    private String token;
}