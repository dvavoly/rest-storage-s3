CREATE TABLE users
(
    id              BIGINT       NOT NULL AUTO_INCREMENT,
    first_name      VARCHAR(150) NOT NULL,
    last_name       VARCHAR(150) NOT NULL,
    email           VARCHAR(150) NOT NULL,
    secret_password VARCHAR(150) NOT NULL,
    user_role       VARCHAR(150) NOT NULL DEFAULT 'USER',
    PRIMARY KEY (id),
    UNIQUE (email)
);