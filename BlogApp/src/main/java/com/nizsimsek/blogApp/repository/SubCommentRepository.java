package com.nizsimsek.blogApp.repository;

import com.nizsimsek.blogApp.model.SubComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubCommentRepository extends JpaRepository<SubComment, String> {
}
