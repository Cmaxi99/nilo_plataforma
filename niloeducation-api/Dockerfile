# Multi-stage build para optimizar el tamaño de la imagen

# Etapa 1: Build
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Copiar archivos de Maven
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Descargar dependencias (se cachea si pom.xml no cambia)
RUN ./mvnw dependency:go-offline -B

# Copiar código fuente
COPY src ./src

# Compilar la aplicación
RUN ./mvnw clean package -DskipTests -B

# Etapa 2: Runtime
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Crear usuario no-root para seguridad
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copiar el JAR compilado desde la etapa de build
COPY --from=builder /app/target/niloeducation-api-*.jar app.jar

# Cambiar permisos
RUN chown -R appuser:appgroup /app

# Cambiar a usuario no-root
USER appuser

# Exponer puerto
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/health || exit 1

# Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]