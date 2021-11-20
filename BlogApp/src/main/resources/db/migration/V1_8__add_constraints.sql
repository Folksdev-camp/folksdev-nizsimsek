--
-- Dökümü yapılmış tablolar için kısıtlamalar
--

--
-- Tablo kısıtlamaları `comment`
--
ALTER TABLE `comment`
    ADD CONSTRAINT `FK8kcum44fvpupyw6f5baccx25c` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    ADD CONSTRAINT `FKs1slvnkuemjsq2kj4h3vhx7i1` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`);

--
-- Tablo kısıtlamaları `post`
--
ALTER TABLE `post`
    ADD CONSTRAINT `FK72mt33dhhs48hf9gcqrq4fxte` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Tablo kısıtlamaları `post_categories`
--
ALTER TABLE `post_categories`
    ADD CONSTRAINT `FKkutvrbjr0w20fuw5hen2w8uij` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`),
    ADD CONSTRAINT `FKs0acwoyjiqr9fm6199mgd6gm1` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`);

--
-- Tablo kısıtlamaları `sub_comment`
--
ALTER TABLE `sub_comment`
    ADD CONSTRAINT `FKihy0jyhesieyafg9xde29r5rj` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    ADD CONSTRAINT `FKpyep9wrxv2s4lxgr14h715983` FOREIGN KEY (`comment_id`) REFERENCES `comment` (`id`);
COMMIT;