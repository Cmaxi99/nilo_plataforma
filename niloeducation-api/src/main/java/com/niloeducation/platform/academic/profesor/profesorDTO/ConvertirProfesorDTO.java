package com.niloeducation.platform.academic.profesor.profesorDTO;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Introspected
@Serdeable
public class ConvertirProfesorDTO {
    @NotNull(message = "El ID de persona es obligatorio")
    private Long idPersona;

    @NotNull(message = "La especialidad es obligatoria")
    private Integer idEspecialidad;

    @NotBlank(message = "La biografía es obligatoria")
    private String biografia;
}
