-- Script de inicialización de bases de datos para Nilo Education
-- Este script se ejecuta automáticamente cuando PostgreSQL inicia por primera vez

-- Base de datos para Keycloak
SELECT 'CREATE DATABASE keycloak'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'keycloak')\gexec

-- Base de datos para la aplicación Nilo Education
SELECT 'CREATE DATABASE niloeducation'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'niloeducation')\gexec


\echo 'Bases de datos keycloak y niloeducation creadas exitosamente'