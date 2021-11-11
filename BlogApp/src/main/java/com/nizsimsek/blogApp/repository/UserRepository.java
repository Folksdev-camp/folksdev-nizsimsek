package com.nizsimsek.blogApp.repository;

import com.nizsimsek.blogApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
