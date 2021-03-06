create table files
(
    id       BIGINT NOT NULL AUTO_INCREMENT,
    file_name VARCHAR(50) NOT NULL,
    location VARCHAR(255),
    user_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);