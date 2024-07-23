CREATE TABLE IF NOT EXISTS chats
(
    id         BIGSERIAL  NOT NULL PRIMARY KEY,
    name       TEXT    NOT NULL,

    UNIQUE (name)
);
