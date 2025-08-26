package com.example.idaustria;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final IdAustriaOidcUserService idAustriaOidcUserService;

    public SecurityConfig(IdAustriaOidcUserService idAustriaOidcUserService) {
        this.idAustriaOidcUserService = idAustriaOidcUserService;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth.requestMatchers("/").permitAll().anyRequest().authenticated())
            .oauth2Login(oauth2 -> oauth2.userInfoEndpoint(userInfo -> userInfo.oidcUserService(idAustriaOidcUserService)));

        return http.build();
    }
}
