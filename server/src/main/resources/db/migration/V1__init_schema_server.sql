-- Create initial tables
-- The comments explain how the tables are related to each other

-- A user can send messages to a group
CREATE TABLE IF NOT EXISTS user
(
    -- Id is expected to be unique and chronologically sortable (e.g. UUID v7)
    -- No need for a index, SQLite automatically creates a index for the primary key
    id          blob NOT NULL UNIQUE PRIMARY KEY,
    type        integer NOT NULL,
    -- Permission contains bitflags where each bit represents a permission scope
    -- Refer to the Java implementation for more details
    permission  integer NOT NULL,
    name        text    NOT NULL,
    email       text    NOT NULL,
    password    text    NOT NULL,
    -- Modified at should be updated when a change is made to the user record
    -- It is expected to be a Unix timestamp in milliseconds
    modified_at text NOT NULL
);

-- A user session is created when a user logs in, creating a session token that is used to authenticate the user
-- Multiple sessions can be created for a user (e.g multiple devices)
CREATE TABLE IF NOT EXISTS user_session
(
    user_id    blob NOT NULL REFERENCES user (id),
    -- A temporary token that is used to authenticate the user
    token      text    NOT NULL,
    expires_at integer NOT NULL
);
CREATE INDEX IF NOT EXISTS user_session_user_id_index ON user_session (user_id);

-- A group is a collection of users that can send messages to each other
CREATE TABLE IF NOT EXISTS "group"
(
    id          blob NOT NULL UNIQUE PRIMARY KEY,
    name        text    NOT NULL,
    description text    NOT NULL,
    modified_at text NOT NULL
);

-- Each message can be sent by a user to a specific group
CREATE TABLE IF NOT EXISTS message
(
    id          blob NOT NULL UNIQUE PRIMARY KEY,
    user_id     blob NOT NULL REFERENCES user (id),
    group_id    blob    NOT NULL REFERENCES "group" (id),
    type        integer NOT NULL,
    content     blob    NOT NULL,
    modified_at text NOT NULL
);
CREATE INDEX IF NOT EXISTS message_user_id_index ON message (user_id);
CREATE INDEX IF NOT EXISTS message_group_id_index ON message (group_id);

-- A user can be in multiple groups
CREATE TABLE IF NOT EXISTS user_group
(
    user_id     blob NOT NULL REFERENCES user (id),
    group_id    blob NOT NULL REFERENCES "group" (id),
    modified_at text NOT NULL
);
CREATE INDEX IF NOT EXISTS user_group_user_id_index ON user_group (user_id);
CREATE INDEX IF NOT EXISTS user_group_group_id_index ON user_group (group_id);

-- Each group can have multiple roles that are assigned to users
CREATE TABLE IF NOT EXISTS role
(
    id          blob NOT NULL PRIMARY KEY,
    group_id    blob NOT NULL REFERENCES "group" (id),
    name        text    NOT NULL,
    permission  integer NOT NULL,
    modified_at text NOT NULL
);
CREATE INDEX IF NOT EXISTS role_group_id_index ON role (group_id);

-- A user can have multiple roles in a specific group
CREATE TABLE IF NOT EXISTS user_role
(
    user_id     blob NOT NULL REFERENCES user (id),
    role_id     blob NOT NULL REFERENCES role (id),
    modified_at text NOT NULL
);
CREATE INDEX IF NOT EXISTS user_role_user_id_index ON user_role (user_id);
CREATE INDEX IF NOT EXISTS user_role_role_id_index ON user_role (role_id);
