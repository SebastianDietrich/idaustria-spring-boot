package com.example.idaustria;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class IdAustriaOidcUserService extends OidcUserService {

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) {
        OidcUser oidcUser = super.loadUser(userRequest);
        // Re-wrap into your custom user
        return new IdAustriaOidcUser(oidcUser.getAuthorities(),
                                     oidcUser.getIdToken(),
                                     oidcUser.getUserInfo());
    }
}
