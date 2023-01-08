CREATE TABLE IF NOT EXISTS users
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    PRIMARY
    KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(128) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS categories
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    PRIMARY
    KEY,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT UQ_CATEGORY_NAME UNIQUE(name)
);

CREATE TABLE IF NOT EXISTS events
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    PRIMARY
    KEY,
    title VARCHAR(120) NOT NULL,
    annotation VARCHAR(2000) NOT NULL,
    description VARCHAR(7000) NOT NULL,
    category_id BIGINT NOT NULL,
    event_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    location_lat DOUBLE PRECISION,
    location_lon DOUBLE PRECISION,
    paid BOOLEAN NOT NULL,
    participant_limit INT NOT NULL,
    confirmed_requests BIGINT,
    request_moderation BOOLEAN NOT NULL,
    initiator_id BIGINT NOT NULL,
    state VARCHAR(100) NOT NULL,
    created_on TIMESTAMP
                           WITHOUT TIME ZONE NOT NULL,
    published_on TIMESTAMP
                           WITHOUT TIME ZONE,
    views BIGINT,
    CONSTRAINT FK_EVENT_CATEGORY FOREIGN KEY
        (
         category_id
            ) REFERENCES categories
            (
             id
                )
        ON DELETE RESTRICT,
    CONSTRAINT FK_EVENT_USER FOREIGN KEY
        (
         initiator_id
            ) REFERENCES users
            (
             id
                )
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS requests
(
    id
        BIGINT
        GENERATED
            BY
            DEFAULT AS
            IDENTITY
        PRIMARY
            KEY,
    event_id
        BIGINT
        NOT
            NULL,
    requester_id
        BIGINT
        NOT
            NULL,
    state
    VARCHAR(100)
    NOT
    NULL,
    created_on
        TIMESTAMP
            WITHOUT
                TIME
                ZONE
        NOT
            NULL,
    CONSTRAINT
        UQ_REQUEST_USER
        UNIQUE
            (
             event_id,
             requester_id
                ),
    CONSTRAINT FK_REQUEST_EVENT FOREIGN KEY
        (
         event_id
            ) REFERENCES events
            (
             id
                ) ON DELETE CASCADE,
    CONSTRAINT FK_REQUEST_USER FOREIGN KEY
        (
         requester_id
            ) REFERENCES users
            (
             id
                )
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS compilations
(
    id
        BIGINT
        GENERATED
            BY
            DEFAULT AS
            IDENTITY
        PRIMARY
            KEY,
    title
    VARCHAR(300)
    NOT
    NULL,
    pinned
        BOOLEAN
        NOT
            NULL
);

CREATE TABLE IF NOT EXISTS compilation_event
(
    id
        BIGINT
        GENERATED
            BY
            DEFAULT AS
            IDENTITY
        PRIMARY
            KEY,
    compilation_id
        BIGINT
        NOT
            NULL,
    event_id
        BIGINT
        NOT
            NULL,
    CONSTRAINT
        UQ_COMPILATION_EVENT
        UNIQUE
            (
             compilation_id,
             event_id
                ),
    CONSTRAINT FK_TO_MANY_COMPILATION FOREIGN KEY
        (
         compilation_id
            ) REFERENCES compilations
            (
             id
                ) ON DELETE CASCADE,
    CONSTRAINT FK_TO_MANY_EVENT FOREIGN KEY
        (
         event_id
            ) REFERENCES events
            (
             id
                )
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS comments
(
    id
              BIGINT
        GENERATED
            BY
            DEFAULT AS
            IDENTITY
        PRIMARY
            KEY,
    text
    VARCHAR(2000) NOT NULL,
    author_id BIGINT NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    event_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT FK_USER_COMMENT FOREIGN KEY
        (
         author_id
            ) REFERENCES users
            (
             id
                )
        ON DELETE CASCADE,
    CONSTRAINT FK_EVENT_COMMENT FOREIGN KEY
        (
         event_id
            ) REFERENCES events
            (
             id
                )
        ON DELETE CASCADE
);