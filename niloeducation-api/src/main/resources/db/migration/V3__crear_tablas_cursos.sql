-- ============================================
-- Flyway Migration V3
-- Descripción: Crear tablas de cursos y contenido educativo
-- Fecha: 2025-10-07
-- ============================================

-- Tabla: curso
-- Cursos ofrecidos por los profesores
CREATE TABLE curso (
    id_curso BIGSERIAL PRIMARY KEY,
    id_profesor BIGINT NOT NULL,
    titulo VARCHAR(255) NOT NULL,
    descripcion TEXT,
    fecha_inicio DATE,
    fecha_fin DATE,
    precio NUMERIC(10,2) CHECK (precio >= 0),
    duracion INTEGER CHECK (duracion > 0),

    -- Foreign Key
    CONSTRAINT fk_curso_profesor
        FOREIGN KEY (id_profesor)
        REFERENCES profesor(id_profesor)
        ON DELETE RESTRICT,

    -- Validación: fecha_fin debe ser posterior a fecha_inicio
    CONSTRAINT chk_curso_fechas
        CHECK (fecha_fin IS NULL OR fecha_inicio IS NULL OR fecha_fin >= fecha_inicio)
);

-- Índices para curso
CREATE INDEX idx_curso_profesor ON curso(id_profesor);
CREATE INDEX idx_curso_titulo ON curso(titulo);
CREATE INDEX idx_curso_fecha_inicio ON curso(fecha_inicio);

-- Tabla: modulo
-- Módulos que componen un curso
CREATE TABLE modulo (
    id_modulo BIGSERIAL PRIMARY KEY,
    id_curso BIGINT NOT NULL,
    titulo VARCHAR(255) NOT NULL,
    descripcion TEXT,
    orden INTEGER NOT NULL CHECK (orden > 0),
    duracion_estimada INTEGER CHECK (duracion_estimada >= 0),

    -- Foreign Key
    CONSTRAINT fk_modulo_curso
        FOREIGN KEY (id_curso)
        REFERENCES curso(id_curso)
        ON DELETE CASCADE,

    -- Un curso no puede tener dos módulos con el mismo orden
    CONSTRAINT uk_modulo_curso_orden
        UNIQUE (id_curso, orden)
);

-- Índices para modulo
CREATE INDEX idx_modulo_curso ON modulo(id_curso);
CREATE INDEX idx_modulo_orden ON modulo(id_curso, orden);

-- Tabla: seccion
-- Secciones (lecciones) dentro de un módulo
CREATE TABLE seccion (
    id_seccion BIGSERIAL PRIMARY KEY,
    id_modulo BIGINT NOT NULL,
    titulo VARCHAR(255) NOT NULL,
    descripcion TEXT,
    orden INTEGER NOT NULL CHECK (orden > 0),
    duracion_estimada INTEGER CHECK (duracion_estimada >= 0),
    tipo_leccion VARCHAR(20) CHECK (tipo_leccion IN ('TEORIA', 'PRACTICA', 'EVALUACION')),

    -- Foreign Key
    CONSTRAINT fk_seccion_modulo
        FOREIGN KEY (id_modulo)
        REFERENCES modulo(id_modulo)
        ON DELETE CASCADE,

    -- Un módulo no puede tener dos secciones con el mismo orden
    CONSTRAINT uk_seccion_modulo_orden
        UNIQUE (id_modulo, orden)
);

-- Índices para seccion
CREATE INDEX idx_seccion_modulo ON seccion(id_modulo);
CREATE INDEX idx_seccion_orden ON seccion(id_modulo, orden);
CREATE INDEX idx_seccion_tipo ON seccion(tipo_leccion);

-- Tabla: material
-- Materiales educativos dentro de una sección
CREATE TABLE material (
    id_material BIGSERIAL PRIMARY KEY,
    id_seccion BIGINT NOT NULL,
    titulo VARCHAR(255) NOT NULL,
    id_tipo_material INTEGER NOT NULL,
    url_contenido VARCHAR(500),
    orden INTEGER NOT NULL CHECK (orden > 0),
    duracion INTEGER CHECK (duracion >= 0),

    -- Foreign Keys
    CONSTRAINT fk_material_seccion
        FOREIGN KEY (id_seccion)
        REFERENCES seccion(id_seccion)
        ON DELETE CASCADE,

    CONSTRAINT fk_material_tipo
        FOREIGN KEY (id_tipo_material)
        REFERENCES tipo_material(id_tipo_material)
        ON DELETE RESTRICT,

    -- Una sección no puede tener dos materiales con el mismo orden
    CONSTRAINT uk_material_seccion_orden
        UNIQUE (id_seccion, orden)
);

-- Índices para material
CREATE INDEX idx_material_seccion ON material(id_seccion);
CREATE INDEX idx_material_tipo ON material(id_tipo_material);
CREATE INDEX idx_material_orden ON material(id_seccion, orden);

-- Comentarios de documentación
COMMENT ON TABLE curso IS 'Cursos ofrecidos en la plataforma';
COMMENT ON COLUMN curso.duracion IS 'Duración total del curso en horas';
COMMENT ON COLUMN curso.precio IS 'Precio del curso en la moneda local';

COMMENT ON TABLE modulo IS 'Módulos que estructuran un curso';
COMMENT ON COLUMN modulo.orden IS 'Orden de presentación del módulo dentro del curso (1, 2, 3...)';
COMMENT ON COLUMN modulo.duracion_estimada IS 'Duración estimada del módulo en minutos';

COMMENT ON TABLE seccion IS 'Secciones o lecciones dentro de un módulo';
COMMENT ON COLUMN seccion.tipo_leccion IS 'TEORIA: contenido teórico, PRACTICA: ejercicios prácticos, EVALUACION: quizzes o exámenes';
COMMENT ON COLUMN seccion.duracion_estimada IS 'Duración estimada de la sección en minutos';

COMMENT ON TABLE material IS 'Materiales educativos (videos, PDFs, enlaces, etc.)';
COMMENT ON COLUMN material.url_contenido IS 'URL del archivo o enlace externo al contenido';
COMMENT ON COLUMN material.duracion IS 'Duración del material en minutos (para videos o audios)';