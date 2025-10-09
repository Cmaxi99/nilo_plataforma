package com.niloeducation.platform.academic.alumno.alumnoEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "estado_alumno")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstadoAlumno {

    @Id
    @Column(name = "estado_alumno", length = 20)
    private String estadoAlumno;

    @Column(name = "descripcion", length = 100)
    private String descripcion;
}
