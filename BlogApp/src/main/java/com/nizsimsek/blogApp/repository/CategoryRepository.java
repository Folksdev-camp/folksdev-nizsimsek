package com.nizsimsek.blogApp.repository;

import com.nizsimsek.blogApp.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, String> {

    List<Category> findAllByIdIn(List<String> idList);
}
