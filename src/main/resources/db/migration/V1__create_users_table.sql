CREATE TABLE users
(
    id              BIGINT       NOT NULL AUTO_INCREMENT,
    first_name      VARCHAR(150) NOT NULL,
    last_name       VARCHAR(150) NOT NULL,
    email           VARCHAR(150) NOT NULL,
    secret_password VARCHAR(255) NOT NULL,
    user_role       INT                   DEFAULT 2,
    status          VARCHAR(10)  NOT NULL DEFAULT 'ACTIVE',
    PRIMARY KEY (id),
    UNIQUE (email)
);