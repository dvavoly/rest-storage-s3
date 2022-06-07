create table files
(
    id       BIGINT NOT NULL AUTO_INCREMENT,
    fileName VARCHAR(50) NOT NULL,
    location VARCHAR(255),
    PRIMARY KEY (id)
);