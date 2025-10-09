package com.niloeducation.platform.academic.profesor.profesorDTO;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Introspected
@Serdeable
public class ProfesorUpdateDTO {
    // Datos de Persona que se pueden actualizar
    private String nombre;
    private String apellido;

    @Email(message = "El email debe ser válido")
    private String email;

    private String telefono;

    // Datos de Profesor que se pueden actualizar
    private Integer idEspecialidad;
    private String biografia;
}
