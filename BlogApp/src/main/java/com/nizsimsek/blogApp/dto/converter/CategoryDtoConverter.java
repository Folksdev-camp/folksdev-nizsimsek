package com.nizsimsek.blogApp.dto.converter;

import com.nizsimsek.blogApp.dto.CategoryDto;
import com.nizsimsek.blogApp.model.Category;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class CategoryDtoConverter extends BaseDtoConverter {

    public CategoryDto convert(Category category) {

        return new CategoryDto(
                category.getId(),
                category.getName(),
                getPostDtos(Objects.requireNonNull(category.getPost()))
        );
    }

    public List<CategoryDto> convertToCategoryDtos(List<Category> categories) {

        return categories
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
