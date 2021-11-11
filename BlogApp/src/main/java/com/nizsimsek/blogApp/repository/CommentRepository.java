package com.nizsimsek.blogApp.repository;

import com.nizsimsek.blogApp.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, String> {
}
