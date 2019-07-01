-- 删除原有数据库
DROP DATABASE `untitled`;

-- 新建数据库
CREATE DATABASE `untitled`;

-- 使用数据库
USE `untitled`;

CREATE TABLE `user`
(
    `user_id`            bigint UNSIGNED NOT NULL AUTO_INCREMENT,
    `user_register_time` datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `user_name`          varchar(20)     NOT NULL UNIQUE,
    `user_password_hash` varchar(32)     NOT NULL,
    `user_password_salt` varchar(32)     NOT NULL,
    `user_phone`         varchar(11)     UNIQUE,
    PRIMARY KEY (`user_id`),
    INDEX (`user_name`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 100000
  DEFAULT CHARSET = utf8mb4;

INSERT IGNORE INTO user (user_name, user_password_hash, user_password_salt, user_phone)
VALUES ('test1', 'test1', 'test1', '12345678910'),
       ('test2', 'test2', 'test2', '12345678911');
