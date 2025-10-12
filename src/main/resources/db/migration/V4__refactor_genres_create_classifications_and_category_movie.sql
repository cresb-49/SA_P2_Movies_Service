-- VXX__refactor_movies_genres_create_classifications_categories_and_relations.sql

-- 1) Quitar columna(s) de género en movies (por si hubo typo previo)
ALTER TABLE IF EXISTS movies
  DROP COLUMN IF EXISTS genere_id;

-- 2) Eliminar la tabla 'generes' (si existe)
DROP TABLE IF EXISTS generes;

-- 3) Nueva tabla classifications
CREATE TABLE IF NOT EXISTS classifications (
  id          UUID PRIMARY KEY,
  name        VARCHAR(100)  NOT NULL,
  description TEXT          NOT NULL,
  created_at   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  CONSTRAINT uq_classifications_name UNIQUE (name)
);

-- Habilita función gen_random_uuid() (PostgreSQL)
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Seed inicial de clasificaciones (Guatemala)
INSERT INTO classifications (id, name, description, created_at)
VALUES (gen_random_uuid(), 'A', 'Apto para todo público', NOW())
ON CONFLICT (name) DO NOTHING;

INSERT INTO classifications (id, name, description, created_at)
VALUES (gen_random_uuid(), 'B', 'Apto para todo público, especialmente películas para mayores de 6 años', NOW())
ON CONFLICT (name) DO NOTHING;

INSERT INTO classifications (id, name, description, created_at)
VALUES (gen_random_uuid(), 'B-12', 'Apto para mayores de 12 años', NOW())
ON CONFLICT (name) DO NOTHING;

INSERT INTO classifications (id, name, description, created_at)
VALUES (gen_random_uuid(), 'B-15', 'Apto para mayores de 15 años', NOW())
ON CONFLICT (name) DO NOTHING;

INSERT INTO classifications (id, name, description, created_at)
VALUES (gen_random_uuid(), 'C', 'Mayores de 18 años', NOW())
ON CONFLICT (name) DO NOTHING;

-- 4) Nueva tabla categories
CREATE TABLE IF NOT EXISTS categories (
  id          UUID PRIMARY KEY,
  name        VARCHAR(150) NOT NULL,
  created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
  updated_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
  CONSTRAINT uq_categories_name UNIQUE (name)
);

-- Datos iniciales para categories
INSERT INTO categories (id, name, created_at, updated_at) VALUES
  (gen_random_uuid(), 'Acción',            NOW(), NOW()),
  (gen_random_uuid(), 'Aventura',          NOW(), NOW()),
  (gen_random_uuid(), 'Comedia',           NOW(), NOW()),
  (gen_random_uuid(), 'Drama',             NOW(), NOW()),
  (gen_random_uuid(), 'Terror',            NOW(), NOW()),
  (gen_random_uuid(), 'Suspenso',          NOW(), NOW()),
  (gen_random_uuid(), 'Ciencia ficción',   NOW(), NOW()),
  (gen_random_uuid(), 'Fantasía',          NOW(), NOW()),
  (gen_random_uuid(), 'Romance',           NOW(), NOW()),
  (gen_random_uuid(), 'Musical',           NOW(), NOW()),
  (gen_random_uuid(), 'Crimen',            NOW(), NOW())
ON CONFLICT (name) DO NOTHING;

-- Índice útil
CREATE INDEX IF NOT EXISTS idx_categories_created_at ON categories (created_at DESC);

-- 5) Tabla de relación categoría–película
CREATE TABLE IF NOT EXISTS category_movie (
  id           UUID PRIMARY KEY,
  category_id  UUID NOT NULL,
  movie_id     UUID NOT NULL,
  CONSTRAINT fk_category_movie_category
    FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE CASCADE,
  CONSTRAINT fk_category_movie_movie
    FOREIGN KEY (movie_id) REFERENCES movies (id) ON DELETE CASCADE,
  CONSTRAINT uq_category_movie UNIQUE (category_id, movie_id)  -- evita duplicados
);

-- Índices para joins
CREATE INDEX IF NOT EXISTS idx_category_movie_category ON category_movie (category_id);
CREATE INDEX IF NOT EXISTS idx_category_movie_movie    ON category_movie (movie_id);

-- 6) Agregar la FK en movies hacia classifications
--    Primero añadimos la columna (nullable por compatibilidad con datos existentes)
ALTER TABLE IF EXISTS movies
    ADD COLUMN IF NOT EXISTS classification_id UUID;

ALTER TABLE movies
    ADD CONSTRAINT fk_movies_classification
    FOREIGN KEY (classification_id)
    REFERENCES classifications (id)
    ON DELETE RESTRICT;

CREATE INDEX IF NOT EXISTS idx_movies_classification ON movies (classification_id);

-- ALTER TABLE movies ALTER COLUMN classification_id SET NOT NULL;

-- 7) Agregamos el atributo active a movies
ALTER TABLE IF EXISTS movies
    ADD COLUMN IF NOT EXISTS active BOOLEAN NOT NULL DEFAULT TRUE;