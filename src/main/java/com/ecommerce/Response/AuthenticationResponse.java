package com.ecommerce.Response;

import lombok.*;

@Setter
@Getter
public class AuthenticationResponse {
    private String token;

    public AuthenticationResponse(String token) {
        this.token = token;
    }

}
