-- ============================================
-- Flyway Migration V1
-- Descripción: Crear tablas de catálogos base
-- Fecha: 2025-10-07
-- ============================================

-- Tabla: estado_alumno
CREATE TABLE estado_alumno(
    id_estado_alumno SERIAL PRIMARY KEY,
    descripcion VARCHAR(50) UNIQUE NOT NULL
);

--Insertar valores iniciales
INSERT INTO estado_alumno(descripcion) VALUES
    ('Activo'),
    ('Inactivo'),
    ('Suspendido');

-- Tabla: especialidad
-- Especialidades de los profesores
CREATE TABLE especialidad(
    id_especialidad SERIAL PRIMARY KEY,
    descripcion VARCHAR(100) UNIQUE NOT NULL
);

--Insertar valores iniciales
INSERT INTO especialidad(descripcion) VALUES
    ('Programación'),
    ('Inteligencia artificial'),
    ('Negocios');

-- Tabla: tipo_material
-- Tipos de materiales educativos
CREATE TABLE tipo_material (
    id_tipo_material SERIAL PRIMARY KEY,
    descripcion VARCHAR(50) UNIQUE NOT NULL
);

-- Insertar valores iniciales
INSERT INTO tipo_material (descripcion) VALUES
    ('Video'),
    ('PDF'),
    ('Imagen'),
    ('Enlace'),
    ('Audio'),
    ('Codigo'),
    ('Precentación'),
    ('Ejercicio');

-- Tabla: estado_inscripcion
-- Estados de las inscripciones
CREATE TABLE estado_inscripcion (
    id_estado_inscripcion SERIAL PRIMARY KEY,
    descripcion VARCHAR(50) UNIQUE NOT NULL
);

-- Insertar valores iniciales
INSERT INTO estado_inscripcion (descripcion) VALUES
    ('Pendiente'),
    ('En progreso'),
    ('Confirmada'),
    ('Cancelada'),
    ('Pausada');

-- Tabla: estado_progreso
-- Estados del progreso de secciones
CREATE TABLE estado_progreso (
    id_estado_progreso SERIAL PRIMARY KEY,
    descripcion VARCHAR(50) UNIQUE NOT NULL
);

-- Insertar valores iniciales
INSERT INTO estado_progreso (descripcion) VALUES
    ('NO_INICIADA'),
    ('EN_PROGRESO'),
    ('COMPLETADA');

-- Comentarios de documentación
COMMENT ON TABLE estado_alumno IS 'Catálogo de estados posibles para alumnos';
COMMENT ON TABLE especialidad IS 'Catálogo de especialidades de profesores';
COMMENT ON TABLE tipo_material IS 'Catálogo de tipos de materiales educativos';
COMMENT ON TABLE estado_inscripcion IS 'Catálogo de estados de inscripciones';
COMMENT ON TABLE estado_progreso IS 'Catálogo de estados de progreso de secciones';

