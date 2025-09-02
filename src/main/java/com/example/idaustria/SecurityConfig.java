package com.example.idaustria;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        .authorizeHttpRequests(auth -> auth.requestMatchers("/me").authenticated().anyRequest().permitAll())
        .oauth2Login(
                oauth -> oauth
                    .userInfoEndpoint(userInfo -> userInfo.oidcUserService(this.idAustriaUserService()))
                    // after login, always forward to /me instead of some sw.js nonsense
                    .defaultSuccessUrl("/me", true))
        .logout(logout -> logout.logoutSuccessUrl("/"));

        return http.build();
    }

    @Bean
    public OidcUserService idAustriaUserService() {
        return new OidcUserService() {
            @Override
            public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
                OidcUser oidcUser = super.loadUser(userRequest);
                return new IdAustriaOidcUser(oidcUser.getAuthorities(), oidcUser.getIdToken());
            }
        };
    }
}