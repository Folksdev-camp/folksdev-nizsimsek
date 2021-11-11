package com.nizsimsek.blogApp.repository;

import com.nizsimsek.blogApp.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, String> {
}
