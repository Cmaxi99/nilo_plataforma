package com.niloeducation.platform.management.keycloak.keycloakConfig;

import io.micronaut.context.annotation.ConfigurationProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties("keycloak")
@Data
public class KeycloakConfig {
    private String serverUrl;
    private String realm;

    private Admin admin;

    private String tokenUrl;
    private String userUrl;
    private String rolesUrl;

    @Data
    public static class Admin {
        private String clientId;
        private String clientSecret;
    }

    public String getAdminClientId() {
        return admin != null ? admin.getClientId() : null;
    }

    public String getAdminClientSecret() {
        return admin != null ? admin.getClientSecret() : null;
    }

    public String getUsersUrl() {
        return userUrl != null ? userUrl : "";
    }

    public String getRolesUrl() {
        return rolesUrl != null ? rolesUrl : "";
    }

}
