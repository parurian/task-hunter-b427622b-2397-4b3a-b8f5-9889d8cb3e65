CREATE TABLE IF NOT EXISTS users
(
    user_id              SERIAL                                                      NOT NULL
        CONSTRAINT users_pkey
            PRIMARY KEY,
    email                VARCHAR(100)                DEFAULT NULL::CHARACTER VARYING NOT NULL,
    password             TEXT                                                        NOT NULL,
    created_at           TIMESTAMP(0) WITH TIME ZONE DEFAULT now()                   NOT NULL,
    updated_at           TIMESTAMP(0) WITH TIME ZONE DEFAULT NULL::TIMESTAMP WITH TIME ZONE,
    confirmation_token   VARCHAR(255)                DEFAULT NULL::CHARACTER VARYING,
    confirmation_sent_at TIMESTAMP(0) WITH TIME ZONE DEFAULT NULL::TIMESTAMP WITH TIME ZONE,
    confirmed_at         TIMESTAMP(0) WITH TIME ZONE DEFAULT NULL::TIMESTAMP WITH TIME ZONE,
    first_name           VARCHAR(255),
    last_name            VARCHAR(255),
    is_active            BOOLEAN                     DEFAULT FALSE
);

CREATE UNIQUE INDEX IF NOT EXISTS users_email_uindex
    ON users (email);

CREATE TABLE IF NOT EXISTS projects
(
    project_id SERIAL                                    NOT NULL
        CONSTRAINT projects_pkey
            PRIMARY KEY,
    name       VARCHAR(255)                              NOT NULL,
    created_at TIMESTAMP(0) WITH TIME ZONE DEFAULT now() NOT NULL
);

CREATE TABLE IF NOT EXISTS tasks
(
    task_id        SERIAL                                    NOT NULL
        CONSTRAINT tasks_pkey
            PRIMARY KEY,
    parent_task_id INTEGER
        CONSTRAINT tasks_parent_task_id_fkey
            REFERENCES tasks
            ON UPDATE RESTRICT ON DELETE RESTRICT,
    name           VARCHAR(255)                              NOT NULL,
    text           TEXT,
    created_at     TIMESTAMP(0) WITH TIME ZONE DEFAULT now() NOT NULL,
    updated_at     TIMESTAMP(0) WITH TIME ZONE,
    project_id     INTEGER                                   NOT NULL
        CONSTRAINT tasks_project_id_fkey
            REFERENCES projects
            ON UPDATE RESTRICT ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS task_assignees
(
    task_assignee_id SERIAL  NOT NULL
        CONSTRAINT task_assignees_pkey
            PRIMARY KEY,
    task_id          INTEGER NOT NULL
        CONSTRAINT task_assignees_task_id_fkey
            REFERENCES tasks
            ON UPDATE RESTRICT ON DELETE RESTRICT,
    assignee_id      INTEGER NOT NULL
        CONSTRAINT task_assignees_assignee_id_fkey
            REFERENCES users
            ON UPDATE RESTRICT ON DELETE RESTRICT,
    assigner_id      INTEGER NOT NULL
        CONSTRAINT task_assignees_assigner_id_fkey
            REFERENCES users
            ON UPDATE RESTRICT ON DELETE RESTRICT,
    CONSTRAINT task_assignees_task_id_assignee_id_key
        UNIQUE (task_id, assignee_id)
);

CREATE TABLE IF NOT EXISTS assignment_history
(
    assignment_history_id SERIAL                                    NOT NULL
        CONSTRAINT assignment_history_pkey
            PRIMARY KEY,
    task_id               INTEGER                                   NOT NULL
        CONSTRAINT assignment_history_task_id_fkey
            REFERENCES tasks,
    assignee_id           INTEGER                                   NOT NULL
        CONSTRAINT assignment_history_assignee_id_fkey
            REFERENCES users,
    created_at            TIMESTAMP(0) WITH TIME ZONE DEFAULT now() NOT NULL,
    is_attached           BOOLEAN                     DEFAULT TRUE  NOT NULL
);

CREATE TABLE IF NOT EXISTS project_owners
(
    project_owner_id SERIAL                                    NOT NULL
        CONSTRAINT project_owners_pkey
            PRIMARY KEY,
    project_id       INTEGER                                   NOT NULL
        CONSTRAINT project_owners_project_id_fkey
            REFERENCES projects
            ON UPDATE RESTRICT ON DELETE RESTRICT,
    user_id          INTEGER                                   NOT NULL
        CONSTRAINT project_owners_user_id_fkey
            REFERENCES users
            ON UPDATE RESTRICT ON DELETE RESTRICT,
    created_at       TIMESTAMP(0) WITH TIME ZONE DEFAULT now() NOT NULL
);

