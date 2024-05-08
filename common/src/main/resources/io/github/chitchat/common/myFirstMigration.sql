CREATE TABLE IF NOT EXISTS User
(

    `id`       integer NOT NULL PRIMARY KEY autoincrement,
    `name`     TEXT,
    `email`    TEXT,
    `password` TEXT

);

CREATE TABLE IF NOT EXISTS Message
(
    id   integer NOT NULL PRIMARY KEY autoincrement,
    messagetype integer,
    from text,
    to   text,
    content text,
    datetime text

);