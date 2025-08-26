package com.example.idaustria;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serial;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collection;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

public class IdAustriaOidcUser extends DefaultOidcUser {
    @Serial
    private static final long serialVersionUID = -4230756683187096769L;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public IdAustriaOidcUser(Collection<? extends GrantedAuthority> authorities, OidcIdToken idToken, OidcUserInfo userInfo) {
        super(authorities, idToken, userInfo);
    }

    public IdAustriaOidcUser(Collection<? extends GrantedAuthority> authorities, OidcIdToken idToken) {
        super(authorities, idToken);
    }

    public String decodeBase64JsonClaim(String claimName) {
        Object value = getClaims().get(claimName);
        if (value instanceof String encoded) {
            try {
                byte[] decoded = Base64.getDecoder().decode(encoded);
                return new String(decoded, StandardCharsets.UTF_8);
            } catch (IllegalArgumentException e) {
                return null; // not valid base64
            }
        }
        return null;
    }

    // Convenience accessors
    @Override
    public String getGivenName() {
        return (String) getClaims().get("given_name");
    }

    @Override
    public String getFamilyName() {
        return (String) getClaims().get("family_name");
    }

    public String getNationality() {
        return Optional
            .ofNullable((String) getClaims().get("urn:eidgvat:attributes.nationality"))
            .map(nat -> new String(Base64.getDecoder().decode(nat), StandardCharsets.UTF_8).substring(2, 5))
            .orElse("");
    }

    public IdAustriaAddress getMainAddress() {
        return getDecodedClaim("urn:eidgvat:attributes.mainAddress", IdAustriaAddress.class);
    }

    public IdAustriaDocument getDocument() {
        return getDecodedClaim("urn:eidgvat:attributes.identificationDocumentData", IdAustriaDocument.class);
    }

    public IdAustriaDocument getPassport() {
        return getDecodedClaim("urn:eidgvat:attributes.passportData", IdAustriaDocument.class);
    }

    private <T> T getDecodedClaim(String claimName, Class<T> targetType) {
        Object encoded = getClaims().get(claimName);
        if (encoded instanceof String encodedStr) {
            try {
                String json = new String(Base64.getDecoder().decode(encodedStr), StandardCharsets.UTF_8);
                return OBJECT_MAPPER.readValue(json, targetType);
            } catch (Exception e) {
                throw new IllegalStateException("Failed to decode base64 JSON claim '" + claimName + "'", e);
            }
        }
        return null;
    }

    public record IdAustriaAddress(
            String Gemeindekennziffer,
            String Gemeindebezeichnung,
            String Postleitzahl,
            String Ortschaft,
            String Strasse,
            String Hausnummer,
            String Stiege,
            String Tuer) {}

    public record IdAustriaDocument(
            String dokumentTyp,
            String dokumentNummer,
            String gueltigVonDatum,
            String gueltigBisDatum,
            String akademischerPrefix,
            String vorName,
            String nachNameZeile1,
            String akademischerPostfix,
            String geschlecht,
            String geburtsDatum,
            String geburtsOrt,
            String groesse,
            String staat,
            String ausstellendeBehoerdeZeile1,
            String ausstellendeBehoerdeZeile2,
            byte[] lichtbild,
            byte[] unterschrift) {}
}
