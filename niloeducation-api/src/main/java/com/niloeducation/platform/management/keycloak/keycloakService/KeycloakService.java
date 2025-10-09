package com.niloeducation.platform.management.keycloak.keycloakService;

import com.niloeducation.platform.management.keycloak.keycloakConfig.KeycloakConfig;
import com.niloeducation.platform.management.keycloak.keycloakDTO.KeycloakUserDTO;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.uri.UriBuilder;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Singleton
public class KeycloakService {

    private static final Logger LOG = LoggerFactory.getLogger(KeycloakService.class);

    private final HttpClient httpClient;
    private final KeycloakConfig config;

    public KeycloakService(@Client HttpClient httpClient, KeycloakConfig config) {
        this.httpClient = httpClient;
        this.config = config;
    }

    /**
     * Obtiene un token de acceso para el Admin API
     */
    private String obtenerTokenAdmin() {
        try {
            Map<String, String> body = new HashMap<>();
            body.put("grant_type", "client_credentials");
            body.put("client_id", config.getAdminClientId());
            body.put("client_secret", config.getAdminClientSecret());

            HttpRequest<?> request = HttpRequest.POST(config.getTokenUrl(), body)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpResponse<Map> response = httpClient.toBlocking().exchange(request, Map.class);

            if (response.body() != null && response.body().containsKey("access_token")) {
                return (String) response.body().get("access_token");
            }

            throw new RuntimeException("No se pudo obtener token de Keycloak");
        } catch (Exception e) {
            LOG.error("Error al obtener token de Keycloak Admin API", e);
            throw new RuntimeException("Error de autenticación con Keycloak", e);
        }
    }

    /**
     * Crea un nuevo usuario en Keycloak
     * @return ID del usuario creado (UUID)
     */
    public String crearUsuario(KeycloakUserDTO userDTO) {
        try {
            String token = obtenerTokenAdmin();

            HttpRequest<?> request = HttpRequest.POST(config.getUsersUrl(), userDTO)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bearerAuth(token);

            HttpResponse<String> response = httpClient.toBlocking().exchange(request, String.class);

            // Keycloak devuelve 201 Created con Location header
            if (response.getStatus().getCode() == 201) {
                // Extraer ID del usuario del Location header
                Optional<String> location = response.header("Location").describeConstable();
                if (location.isPresent()) {
                    String userId = location.get().substring(location.get().lastIndexOf('/') + 1);
                    LOG.info("Usuario creado en Keycloak con ID: {}", userId);
                    return userId;
                }
            }

            throw new RuntimeException("No se pudo crear usuario en Keycloak");
        } catch (Exception e) {
            LOG.error("Error al crear usuario en Keycloak", e);
            throw new RuntimeException("Error al crear usuario en Keycloak: " + e.getMessage(), e);
        }
    }

    /**
     * Asigna un rol del realm a un usuario
     */
    public void asignarRol(String userId, String nombreRol) {
        try {
            String token = obtenerTokenAdmin();

            // 1. Obtener detalles del rol
            String rolUrl = config.getRolesUrl() + "/" + nombreRol;
            HttpRequest<?> getRolRequest = HttpRequest.GET(rolUrl)
                    .bearerAuth(token);

            HttpResponse<Map> rolResponse = httpClient.toBlocking().exchange(getRolRequest, Map.class);
            Map<String, Object> rol = rolResponse.body();

            // 2. Asignar rol al usuario
            String assignRoleUrl = config.getUsersUrl() + "/" + userId + "/role-mappings/realm";
            HttpRequest<?> assignRequest = HttpRequest.POST(assignRoleUrl, List.of(rol))
                    .contentType(MediaType.APPLICATION_JSON)
                    .bearerAuth(token);

            httpClient.toBlocking().exchange(assignRequest);
            LOG.info("Rol {} asignado al usuario {}", nombreRol, userId);
        } catch (Exception e) {
            LOG.error("Error al asignar rol {} al usuario {}", nombreRol, userId, e);
            throw new RuntimeException("Error al asignar rol en Keycloak", e);
        }
    }

    /**
     * Verifica si un usuario existe por email
     */
    public boolean existeUsuarioPorEmail(String email) {
        try {
            String token = obtenerTokenAdmin();

            URI uri = UriBuilder.of(config.getUsersUrl())
                    .queryParam("email", email)
                    .queryParam("exact", "true")
                    .build();

            HttpRequest<?> request = HttpRequest.GET(uri)
                    .bearerAuth(token);

            HttpResponse<List> response = httpClient.toBlocking().exchange(request, List.class);
            return response.body() != null && !response.body().isEmpty();
        } catch (Exception e) {
            LOG.error("Error al verificar existencia de usuario por email", e);
            return false;
        }
    }

    /**
     * Verifica si un usuario existe por username
     */
    public boolean existeUsuarioPorUsername(String username) {
        try {
            String token = obtenerTokenAdmin();

            URI uri = UriBuilder.of(config.getUsersUrl())
                    .queryParam("username", username)
                    .queryParam("exact", "true")
                    .build();

            HttpRequest<?> request = HttpRequest.GET(uri)
                    .bearerAuth(token);

            HttpResponse<List> response = httpClient.toBlocking().exchange(request, List.class);
            return response.body() != null && !response.body().isEmpty();
        } catch (Exception e) {
            LOG.error("Error al verificar existencia de usuario por username", e);
            return false;
        }
    }

    /**
     * Obtiene un usuario de Keycloak por ID
     */
    public Optional<KeycloakUserDTO> obtenerUsuario(String userId) {
        try {
            String token = obtenerTokenAdmin();

            String url = config.getUsersUrl() + "/" + userId;
            HttpRequest<?> request = HttpRequest.GET(url)
                    .bearerAuth(token);

            HttpResponse<KeycloakUserDTO> response = httpClient.toBlocking().exchange(request, KeycloakUserDTO.class);
            return Optional.ofNullable(response.body());
        } catch (Exception e) {
            LOG.error("Error al obtener usuario de Keycloak", e);
            return Optional.empty();
        }
    }

    /**
     * Actualiza un usuario en Keycloak
     */
    public void actualizarUsuario(String userId, KeycloakUserDTO userDTO) {
        try {
            String token = obtenerTokenAdmin();

            String url = config.getUsersUrl() + "/" + userId;
            HttpRequest<?> request = HttpRequest.PUT(url, userDTO)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bearerAuth(token);

            httpClient.toBlocking().exchange(request);
            LOG.info("Usuario {} actualizado en Keycloak", userId);
        } catch (Exception e) {
            LOG.error("Error al actualizar usuario en Keycloak", e);
            throw new RuntimeException("Error al actualizar usuario en Keycloak", e);
        }
    }

    /**
     * Elimina un usuario de Keycloak
     */
    public void eliminarUsuario(String userId) {
        try {
            String token = obtenerTokenAdmin();

            String url = config.getUsersUrl() + "/" + userId;
            HttpRequest<?> request = HttpRequest.DELETE(url)
                    .bearerAuth(token);

            httpClient.toBlocking().exchange(request);
            LOG.info("Usuario {} eliminado de Keycloak", userId);
        } catch (Exception e) {
            LOG.error("Error al eliminar usuario de Keycloak", e);
            throw new RuntimeException("Error al eliminar usuario de Keycloak", e);
        }
    }

    /**
     * Restablece la contraseña de un usuario
     */
    public void restablecerPassword(String userId, String nuevaPassword, boolean temporal) {
        try {
            String token = obtenerTokenAdmin();

            KeycloakUserDTO.CredentialDTO credential = KeycloakUserDTO.CredentialDTO.builder()
                    .type("password")
                    .value(nuevaPassword)
                    .temporary(temporal)
                    .build();

            String url = config.getUsersUrl() + "/" + userId + "/reset-password";
            HttpRequest<?> request = HttpRequest.PUT(url, credential)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bearerAuth(token);

            httpClient.toBlocking().exchange(request);
            LOG.info("Contraseña restablecida para usuario {}", userId);
        } catch (Exception e) {
            LOG.error("Error al restablecer contraseña", e);
            throw new RuntimeException("Error al restablecer contraseña", e);
        }
    }

}
