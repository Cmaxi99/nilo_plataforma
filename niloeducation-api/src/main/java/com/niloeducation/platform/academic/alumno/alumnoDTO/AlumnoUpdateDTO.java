package com.niloeducation.platform.academic.alumno.alumnoDTO;

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
public class AlumnoUpdateDTO {
    private String nombre;
    private String apellido;

    @Email(message = "El email debe ser válido")
    private String email;

    private String telefono;

    private String nivelEducativo;
}
