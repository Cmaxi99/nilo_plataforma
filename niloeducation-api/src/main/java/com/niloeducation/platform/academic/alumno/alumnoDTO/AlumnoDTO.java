package com.niloeducation.platform.academic.alumno.alumnoDTO;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * DTO para Alumno (Opción B - DTO Plano)
 * Incluye todos los datos de Persona en el mismo nivel
 * para facilitar el uso en APIs REST
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Introspected
@Serdeable
public class AlumnoDTO {

    // ========== Datos del Alumno ==========

    private Long idAlumno;

    @NotBlank(message = "El legajo es obligatorio")
    private String legajo;

    private String nivelEducativo; // Ej: "Secundaria", "Universidad", "Posgrado"

    @NotBlank(message = "El estado es obligatorio")
    private String estadoAlumno; // "Activo", "Inactivo", "Suspendido"

    private LocalDateTime fechaPrimeraInscripcion; // Cuándo se convirtió en alumno

    // ========== Datos de la Persona (denormalizados) ==========

    private Long idPersona;

    private String keycloakUserId; // ID del usuario en Keycloak

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    private String email;

    private String telefono;

    private LocalDateTime fechaRegistro; // Cuándo se registró como usuario

    // ========== Métodos Auxiliares ==========

    /**
     * Retorna el nombre completo del alumno
     */
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    /**
     * Verifica si el alumno está activo
     */
    public boolean isActivo() {
        return "Activo".equalsIgnoreCase(estadoAlumno);
    }

    /**
     * Calcula días desde que se volvió alumno
     */
    public long getDiasDesdeInscripcion() {
        if (fechaPrimeraInscripcion == null) {
            return 0;
        }
        return java.time.Duration.between(fechaPrimeraInscripcion, LocalDateTime.now()).toDays();
    }
}