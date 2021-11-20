--
-- Dökümü yapılmış tablolar için indeksler
--

--
-- Tablo için indeksler `comment`
--
ALTER TABLE `comment`
    ADD KEY `FK8kcum44fvpupyw6f5baccx25c` (`user_id`),
    ADD KEY `FKs1slvnkuemjsq2kj4h3vhx7i1` (`post_id`);

--
-- Tablo için indeksler `post`
--
ALTER TABLE `post`
    ADD KEY `FK72mt33dhhs48hf9gcqrq4fxte` (`user_id`);

--
-- Tablo için indeksler `post_categories`
--
ALTER TABLE `post_categories`
    ADD KEY `FKs0acwoyjiqr9fm6199mgd6gm1` (`category_id`),
    ADD KEY `FKkutvrbjr0w20fuw5hen2w8uij` (`post_id`);

--
-- Tablo için indeksler `sub_comment`
--
ALTER TABLE `sub_comment`
    ADD KEY `FKihy0jyhesieyafg9xde29r5rj` (`user_id`),
    ADD KEY `FKpyep9wrxv2s4lxgr14h715983` (`comment_id`);
