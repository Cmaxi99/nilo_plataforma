-- ============================================
-- Flyway Migration V2
-- Descripción: Crear tablas de personas
-- Fecha: 2025-10-07
-- ============================================

-- Tabla: persona
-- Tabla base que contiene información común de alumnos y profesores
CREATE TABLE persona (
    id_persona BIGSERIAL PRIMARY KEY,
    keycloak_user_id VARCHAR(255) UNIQUE NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    email VARCHAR(100) NOT NULL,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índice para búsquedas por keycloak_user_id
CREATE INDEX idx_persona_keycloak_user_id ON persona(keycloak_user_id);

-- Tabla: alumno
-- Información específica de alumnos
CREATE TABLE alumno (
    id_alumno BIGSERIAL PRIMARY KEY,
    id_persona BIGINT UNIQUE NOT NULL,
    legajo VARCHAR(20) UNIQUE NOT NULL,
    nivel_educativo VARCHAR(50),
    id_estado_alumno INTEGER NOT NULL,
    fecha_primera_inscripcion TIMESTAMP,

    -- Foreign Keys
    CONSTRAINT fk_alumno_persona
        FOREIGN KEY (id_persona)
        REFERENCES persona(id_persona)
        ON DELETE CASCADE,

    CONSTRAINT fk_alumno_estado
        FOREIGN KEY (id_estado_alumno)
        REFERENCES estado_alumno(id_estado_alumno)
        ON DELETE RESTRICT
);

-- Índices para alumno
CREATE INDEX idx_alumno_legajo ON alumno(legajo);
CREATE INDEX idx_alumno_estado ON alumno(id_estado_alumno);

-- Tabla: profesor
-- Información específica de profesores
CREATE TABLE profesor (
    id_profesor BIGSERIAL PRIMARY KEY,
    id_persona BIGINT UNIQUE NOT NULL,
    id_especialidad INTEGER NOT NULL,
    biografia TEXT,

    -- Foreign Keys
    CONSTRAINT fk_profesor_persona
        FOREIGN KEY (id_persona)
        REFERENCES persona(id_persona)
        ON DELETE CASCADE,

    CONSTRAINT fk_profesor_especialidad
        FOREIGN KEY (id_especialidad)
        REFERENCES especialidad(id_especialidad)
        ON DELETE RESTRICT
);

-- Índices para profesor
CREATE INDEX idx_profesor_especialidad ON profesor(id_especialidad);

-- Comentarios de documentación
COMMENT ON TABLE persona IS 'Tabla base con información común de alumnos y profesores. Vinculada con Keycloak via keycloak_user_id';
COMMENT ON COLUMN persona.keycloak_user_id IS 'ID del usuario en Keycloak (claim "sub" del JWT)';


COMMENT ON TABLE alumno IS 'Información específica de estudiantes';
COMMENT ON COLUMN alumno.legajo IS 'Matrícula única del alumno (ej: A2025001)';

COMMENT ON TABLE profesor IS 'Información específica de profesores/instructores';