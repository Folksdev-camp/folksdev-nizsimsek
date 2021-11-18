package com.nizsimsek.blogApp.service;

import com.nizsimsek.blogApp.dto.CategoryDto;
import com.nizsimsek.blogApp.dto.converter.CategoryDtoConverter;
import com.nizsimsek.blogApp.dto.request.CreateCategoryReq;
import com.nizsimsek.blogApp.dto.request.UpdateCategoryReq;
import com.nizsimsek.blogApp.exception.GeneralNotFoundException;
import com.nizsimsek.blogApp.model.Category;
import com.nizsimsek.blogApp.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryDtoConverter categoryDtoConverter;

    public CategoryService(CategoryRepository categoryRepository,
                           CategoryDtoConverter categoryDtoConverter) {
        this.categoryRepository = categoryRepository;
        this.categoryDtoConverter = categoryDtoConverter;
    }

    public CategoryDto createCategory(CreateCategoryReq createCategoryReq) {

        Category category = new Category(
                createCategoryReq.getName()
        );

        return categoryDtoConverter.convert(categoryRepository.save(category));
    }

    public List<CategoryDto> getAllCategories() {

        return categoryDtoConverter.convertToCategoryDtos(findAllCategories());
    }

    public CategoryDto getCategoryById(String id) {

        return categoryDtoConverter.convert(findCategoryById(id));
    }

    public CategoryDto updateCategory(String id, UpdateCategoryReq updateCategoryReq) {

        Category category = findCategoryById(id);

        Category updatedCategory = new Category(
                category.getId(),
                updateCategoryReq.getName()
        );

        return categoryDtoConverter.convert(categoryRepository.save(updatedCategory));
    }

    public void deleteCategoryById(String id) {

        categoryRepository.deleteById(id);
    }

    protected Category findCategoryById(String id) {

        return categoryRepository.findById(id)
                .orElseThrow(() -> new GeneralNotFoundException("Category could not find by id : " + id));
    }

    protected List<Category> findAllCategories() {

        return categoryRepository.findAll();
    }

    protected List<Category> getCategories(List<String> idList) {

        return Optional.of(categoryRepository.findAllByIdIn(idList))
                .filter(category -> !category.isEmpty())
                .orElseThrow(() -> new GeneralNotFoundException("Kategori bulunamadı verilen idler : " + idList));
    }
}
