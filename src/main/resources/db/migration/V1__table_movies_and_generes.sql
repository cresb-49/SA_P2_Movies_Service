
CREATE TABLE generes (
    id      UUID PRIMARY KEY,
    name    VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE movies (
    id          UUID PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    duration    INT NOT NULL,
    sinopsis    TEXT NOT NULL,
    url_image    VARCHAR(512) NOT NULL,
    genere_id    UUID NOT NULL REFERENCES generes(id) ON DELETE RESTRICT
);