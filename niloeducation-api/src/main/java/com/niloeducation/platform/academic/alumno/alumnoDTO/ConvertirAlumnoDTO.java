package com.niloeducation.platform.academic.alumno.alumnoDTO;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotNull;
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
public class ConvertirAlumnoDTO {
    @NotNull(message = "El ID de persona es obligatorio")
    private Long idPersona;

    private String nivelEducativo;
}
