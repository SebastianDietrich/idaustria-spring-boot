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
            .of(
                "issuer",
                user.getIssuer(),
                "subject",
                user.getSubject(),
                "given_name",
                user.getGivenName(),
                "family_name",
                user.getFamilyName(),
                "main_address",
                user.getMainAddress(),
                "document_data",
                user.getDocument());
    }
}
