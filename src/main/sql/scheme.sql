# -- 删除原有数据库
# DROP DATABASE `untitled`;
#
# -- 新建数据库
# CREATE DATABASE `untitled`;

-- 使用数据库
USE `untitled`;

# CREATE TABLE `user`
# (
#     `user_id`            int UNSIGNED NOT NULL AUTO_INCREMENT,
#     `user_register_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
#     `user_name`          varchar(20)  NOT NULL UNIQUE,
#     `user_password_hash` varchar(32)  NOT NULL,
#     `user_password_salt` varchar(32)  NOT NULL,
#     `user_phone`         varchar(11) UNIQUE,
#     PRIMARY KEY (`user_id`),
#     INDEX (`user_name`)
# ) ENGINE = InnoDB
#   AUTO_INCREMENT = 100000
#   DEFAULT CHARSET = utf8mb4;
#
# CREATE TABLE `admin`
# (
#     `user_id`            int UNSIGNED NOT NULL AUTO_INCREMENT,
#     `user_register_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
#     `user_name`          varchar(20)  NOT NULL UNIQUE,
#     `user_password_hash` varchar(32)  NOT NULL,
#     `user_password_salt` varchar(32)  NOT NULL,
#     `user_phone`         varchar(11) UNIQUE,
#     PRIMARY KEY (`user_id`),
#     INDEX (`user_name`)
# ) ENGINE = InnoDB
#   AUTO_INCREMENT = 100000
#   DEFAULT CHARSET = utf8mb4;

DROP TABLE `employment`;
DROP TABLE `user_skill`;
DROP TABLE `skill`;
DROP TABLE `duty`;

CREATE TABLE `duty`
(
    `duty_id`   int UNSIGNED NOT NULL AUTO_INCREMENT,
    `duty_name` varchar(20)  NOT NULL UNIQUE,
    `description` varchar(255),
    PRIMARY KEY (`duty_id`),
    INDEX (`duty_name`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 0
  DEFAULT CHARSET = utf8mb4;


CREATE TABLE `skill`
(
    `skill_id`   int UNSIGNED NOT NULL AUTO_INCREMENT,
    `skill_name` varchar(20)  NOT NULL UNIQUE,
    PRIMARY KEY (`skill_id`),
    INDEX (`skill_name`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 0
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `employment`
(
    `employment_id` int UNSIGNED NOT NULL,
    `duty_id`       int UNSIGNED NOT NULL,
    `skill_id`      int UNSIGNED NOT NULL,
    PRIMARY KEY (`employment_id`, `duty_id`, `skill_id`),
    FOREIGN KEY (`duty_id`) REFERENCES duty (`duty_id`),
    FOREIGN KEY (`skill_id`) REFERENCES skill (`skill_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `user_skill`
(
    `user_id`  int UNSIGNED NOT NULL,
    `skill_id` int UNSIGNED NOT NULL,
    `proficiency` int UNSIGNED NOT NULL DEFAULT 3,
    PRIMARY KEY (`user_id`, `skill_id`),
    FOREIGN KEY (`user_id`) REFERENCES user (`user_id`),
    FOREIGN KEY (`skill_id`) REFERENCES skill (`skill_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;