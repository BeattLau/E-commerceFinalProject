package com.ecommerce.Config;
import com.ecommerce.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationProvider authenticationProvider) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeRequests(request -> request
                        .requestMatchers(
                                "/*/api-docs",
                                "/*/api-docs/**",
                                "/swagger-resources",
                                "/swagger-resources/**",
                                "/configuration/ui",
                                "/configuration/security",
                                "/swagger-ui/**",
                                "/swagger-ui/index.html")
                        .permitAll()
                        .requestMatchers("/api/v1/login", "/api/v1/register").permitAll()
                        .requestMatchers("/admin").hasAuthority("ADMIN")
                        .requestMatchers("/users/all", "/users/save", "/users/user-login").hasAuthority("ADMIN")
                        .requestMatchers(
                                "/api/v1/products/name/{name}", "/api/v1/products/id/{productId}", "/api/v1/products/all").permitAll()
                        .requestMatchers(
                                "/api/v1/products/add", "/api/v1/products/update/", "/api/v1/products/delete/")
                        .hasAnyRole("ADMIN", "SELLER")
                        .requestMatchers("/products/all", "/products/{productName}", "/products/{productId}", "/products/add", "/products/update/{productId}", "/products/delete/{productId}").authenticated()
                        .requestMatchers("/api/v1/cart/contents", "/api/v1/cart/add/{productId}", "/api/v1/cart/delete/{productId}").authenticated()
                        .requestMatchers("/api/v1/orders/{userId}", "/api/v1/orders/place-order-from-cart", "/api/v1/orders/{orderId}", "/api/v1/orders/order-history", "/api/v1/orders/{orderId}").authenticated()
                        .requestMatchers("/api/v1/orders/{orderId}/status/{newStatus}", "/api/v1/order/all").hasAuthority("ADMIN")
                        .anyRequest().authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationProvider authenticationProvider(UserService userService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService.getUserDetailService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}