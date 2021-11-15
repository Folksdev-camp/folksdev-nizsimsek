package com.nizsimsek.blogApp.dto.converter;

import com.nizsimsek.blogApp.dto.CategoryDto;
import com.nizsimsek.blogApp.model.Category;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CategoryDtoConverter extends BaseDtoConverter {

    public CategoryDto convert(Category category) {

        return new CategoryDto(
                category.getId(),
                category.getName(),
                getPostDtos(Objects.requireNonNull(category.getPost()))
        );
    }
}
