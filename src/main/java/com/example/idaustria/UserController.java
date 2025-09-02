package com.example.idaustria;

import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/me")
    public Map<String, Object> me(@AuthenticationPrincipal IdAustriaOidcUser user) {
        return Map
                .ofEntries(
                    Map.entry("issuer", user.getIssuer()),
                    Map.entry("subject", user.getSubject()),
                    Map.entry("given_name", user.getGivenName()),
                    Map.entry("family_name", user.getFamilyName()),
                    Map.entry("main_address", user.getMainAddress()),
                    Map.entry("document_data", user.getDocument()),
                    Map.entry("nationality", user.getNationality()),
                    Map.entry("passport", user.getPassport()),
                    Map.entry("authorities", user.getAuthorities()),
                    Map.entry("claims", user.getClaims()),
                    Map.entry("gender", user.getGender()),
                Map.entry("birthdate", user.getBirthdate()),
                    Map.entry("vehicle", user.getVehicle()),
                    Map.entry("main_address_registration_date", user.getMainAddressRegistrationDate()));
    }
}
