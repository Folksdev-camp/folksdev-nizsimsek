CREATE TABLE IF NOT EXISTS `post` (
    `id` varchar(255) PRIMARY KEY NOT NULL,
    `content` TEXT DEFAULT NULL,
    `created_date` datetime DEFAULT NULL,
    `dislikes` bigint NOT NULL,
    `likes` bigint NOT NULL,
    `title` varchar(255) DEFAULT NULL,
    `updated_date` datetime DEFAULT NULL,
    `user_id` varchar(255) DEFAULT NULL
)