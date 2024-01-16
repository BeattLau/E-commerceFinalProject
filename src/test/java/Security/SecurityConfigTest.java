package Security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @InjectMocks
    private SecurityConfig securityConfig;
    @Test
    void configure_ShouldConfigureSecurity() throws Exception {
        HttpSecurity httpSecurity = Mockito.mock(HttpSecurity.class);
        securityConfig.configure(httpSecurity);
        Mockito.verify(httpSecurity, Mockito.times(1)).cors().and().csrf().disable();
        Mockito.verify(httpSecurity, Mockito.times(1)).authorizeRequests();
        Mockito.verify(httpSecurity, Mockito.times(1)).antMatchers("/api/v1/register", "/api/v1/login").permitAll();
        Mockito.verify(httpSecurity, Mockito.times(1)).antMatchers(HttpMethod.OPTIONS, "/**").permitAll();
        Mockito.verify(httpSecurity, Mockito.times(1)).anyRequest().authenticated();
        Mockito.verify(httpSecurity, Mockito.times(1)).exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint);
        Mockito.verify(httpSecurity, Mockito.times(1)).addFilterAfter(Mockito.any(JwtAuthenticationFilter.class), Mockito.eq(UsernamePasswordAuthenticationFilter.class));
    }
    @Test
    void configureAuthentication_ShouldConfigureAuthenticationManagerBuilder() throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = Mockito.mock(AuthenticationManagerBuilder.class);
        securityConfig.configure(authenticationManagerBuilder);
        Mockito.verify(authenticationManagerBuilder, Mockito.times(1)).userDetailsService(userDetailsService);
        Mockito.verify(authenticationManagerBuilder, Mockito.times(1)).passwordEncoder(securityConfig.passwordEncoder());
    }
}
