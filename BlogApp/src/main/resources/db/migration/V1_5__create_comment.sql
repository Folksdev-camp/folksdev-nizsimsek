CREATE TABLE IF NOT EXISTS `comment` (
    `id` varchar(255) PRIMARY KEY NOT NULL,
    `content` varchar(5000) DEFAULT NULL,
    `created_date` datetime DEFAULT NULL,
    `dislikes` bigint NOT NULL,
    `likes` bigint NOT NULL,
    `updated_date` datetime DEFAULT NULL,
    `user_id` varchar(255) DEFAULT NULL,
    `post_id` varchar(255) DEFAULT NULL
)