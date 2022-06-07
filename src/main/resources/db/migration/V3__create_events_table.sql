create table events
(
    id          BIGINT    NOT NULL AUTO_INCREMENT,
    upload_time TIMESTAMP NOT NULL,
    user_id     BIGINT,
    file_id     BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (file_id) REFERENCES files (id)
);