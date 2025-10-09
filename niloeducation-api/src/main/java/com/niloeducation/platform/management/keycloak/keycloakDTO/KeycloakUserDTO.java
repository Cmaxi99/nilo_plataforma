package com.niloeducation.platform.management.keycloak.keycloakDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Introspected
@Serdeable
public class KeycloakUserDTO {
    private String id; // ID del usuario en Keycloak (UUID)

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private Boolean enabled; // true = cuenta habilitada

    @JsonProperty("emailVerified")
    private Boolean emailVerified; // true = email verificado

    private List<CredentialDTO> credentials; // Para establecer contraseña

    private List<String> realmRoles; // Roles del realm

    private Map<String, List<String>> attributes; // Atributos adicionales

    /**
     * DTO interno para credenciales
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Introspected
    @Serdeable
    public static class CredentialDTO {
        private String type; // "password"
        private String value; // la contraseña
        private Boolean temporary; // true = usuario debe cambiar contraseña
    }
}
