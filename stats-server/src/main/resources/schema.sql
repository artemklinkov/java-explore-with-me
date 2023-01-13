CREATE TABLE IF NOT EXISTS hits
(
    hit_id
            BIGINT
        GENERATED
            BY
            DEFAULT AS
            IDENTITY
        PRIMARY
            KEY,
    app
    VARCHAR(255) NOT NULL,
    uri VARCHAR NOT NULL,
    ip VARCHAR(46) NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL
);