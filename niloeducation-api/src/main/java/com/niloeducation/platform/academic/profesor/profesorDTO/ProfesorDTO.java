package com.niloeducation.platform.academic.profesor.profesorDTO;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Introspected
@Serdeable
public class ProfesorDTO {

    // ========== Datos del Profesor ==========

    private Long idProfesor;

    @NotNull(message = "La especialidad es obligatoria")
    private Integer idEspecialidad;

    private String nombreEspecialidad; // Nombre legible de la especialidad

    private String biografia;

    private LocalDateTime fechaAprobacion; // Cuándo fue aprobado como profesor

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

    // ========== Estadísticas (futuro) ==========

    private Integer cantidadCursos; // Cantidad de cursos dictados
    private Integer cantidadAlumnos; // Cantidad total de alumnos

    // ========== Métodos Auxiliares ==========

    /**
     * Retorna el nombre completo del profesor
     */
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    /**
     * Retorna la biografía resumida (primeros 100 caracteres)
     */
    public String getBiografiaResumida() {
        if (biografia == null || biografia.length() <= 100) {
            return biografia;
        }
        return biografia.substring(0, 100) + "...";
    }

    /**
     * Calcula días desde que fue aprobado
     */
    public long getDiasDesdeAprobacion() {
        if (fechaAprobacion == null) {
            return 0;
        }
        return java.time.Duration.between(fechaAprobacion, LocalDateTime.now()).toDays();
    }
}
