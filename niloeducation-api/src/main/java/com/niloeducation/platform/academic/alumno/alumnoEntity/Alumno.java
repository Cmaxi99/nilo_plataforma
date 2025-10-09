package com.niloeducation.platform.academic.alumno.alumnoEntity;

import com.niloeducation.platform.academic.persona.personaEntity.Persona;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Table(name = "alumno")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Alumno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alumno")
    private Long idAlumno;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_persona", nullable = false)
    private Persona persona;

    @Column(name = "legajo", unique = true, nullable = false, length = 50)
    private String legajo;

    @Column(name = "nivel_educativo", length = 100)
    private String nivelEducativo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado_alumno")
    private EstadoAlumno estadoAlumno;

    @Column(name = "fecha_primera_inscripcion", nullable = true)
    private LocalDateTime fechaPrimeraInscripcion;
}
