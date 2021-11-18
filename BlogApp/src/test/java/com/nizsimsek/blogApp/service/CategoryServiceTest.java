package com.nizsimsek.blogApp.service;

import com.nizsimsek.blogApp.TestSupport;
import com.nizsimsek.blogApp.dto.CategoryDto;
import com.nizsimsek.blogApp.dto.converter.CategoryDtoConverter;
import com.nizsimsek.blogApp.dto.request.CreateCategoryReq;
import com.nizsimsek.blogApp.dto.request.UpdateCategoryReq;
import com.nizsimsek.blogApp.exception.GeneralNotFoundException;
import com.nizsimsek.blogApp.model.Category;
import com.nizsimsek.blogApp.repository.CategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CategoryServiceTest extends TestSupport {

    private CategoryRepository categoryRepository;
    private CategoryDtoConverter categoryDtoConverter;

    private CategoryService categoryService;

    @BeforeEach
    void setUp() {

        categoryRepository = mock(CategoryRepository.class);
        categoryDtoConverter = mock(CategoryDtoConverter.class);

        categoryService = new CategoryService(categoryRepository, categoryDtoConverter);
    }

    @Test
    void testCreateCategory_whenCreateCategoryReqValid_shouldReturnCategoryDto() {

        CreateCategoryReq createCategoryReq = generateCreateCategoryReq();
        Category category = generateCategory();
        CategoryDto categoryDto = generateCategoryDto();

        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryDtoConverter.convert(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.createCategory(createCategoryReq);

        assertEquals(categoryDto, result);

        verify(categoryRepository).save(category);
        verify(categoryDtoConverter).convert(category);
    }

    @Test
    void testGetAllCategories_shouldReturnCategoryDtos() {

        List<Category> categories = generateCategories();
        List<CategoryDto> categoryDtos = generateCategoryDtos();

        when(categoryRepository.findAll()).thenReturn(categories);
        when(categoryDtoConverter.convertToCategoryDtos(categories)).thenReturn(categoryDtos);

        List<CategoryDto> result = categoryService.getAllCategories();

        assertEquals(categoryDtos, result);

        verify(categoryRepository).findAll();
        verify(categoryDtoConverter).convertToCategoryDtos(categories);
    }

    @Test
    void testGetCategoryById_whenIdNotExist_shouldThrowGeneralNotFoundException() {

        when(categoryRepository.findById("id")).thenThrow(GeneralNotFoundException.class);

        assertThrows(GeneralNotFoundException.class, () -> categoryService.getCategoryById("id"));

        verify(categoryRepository).findById("id");
        verifyNoInteractions(categoryDtoConverter);
    }

    @Test
    void testGetCategoryById_whenIdExist_shouldReturnCategoryDto() {

        Category category = generateCategory();
        CategoryDto categoryDto = generateCategoryDto();

        when(categoryRepository.findById("id")).thenReturn(java.util.Optional.ofNullable(category));
        assert category != null;
        when(categoryDtoConverter.convert(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.getCategoryById("id");

        assertEquals(categoryDto, result);

        verify(categoryRepository).findById("id");
        verify(categoryDtoConverter).convert(category);
    }

    @Test
    void testUpdateCategory_whenUpdateCategoryReqInValid_shouldThrowGeneralNotFoundException() {

        UpdateCategoryReq updateCategoryReq = generateUpdateCategoryReq();

        when(categoryRepository.findById("id")).thenThrow(GeneralNotFoundException.class);

        assertThrows(GeneralNotFoundException.class, () -> categoryService.updateCategory("id", updateCategoryReq));

        verify(categoryRepository).findById("id");
        verifyNoInteractions(categoryDtoConverter);
    }

    @Test
    void testUpdateCategory_whenUpdateCategoryReqValid_shouldReturnCategoryDto() {

        UpdateCategoryReq updateCategoryReq = generateUpdateCategoryReq();
        Category category = generateCategory();
        CategoryDto categoryDto = generateCategoryDto();

        when(categoryRepository.findById("id")).thenReturn(java.util.Optional.ofNullable(category));
        assert category != null;
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryDtoConverter.convert(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.updateCategory("id", updateCategoryReq);

        assertEquals(categoryDto, result);

        verify(categoryRepository).findById("id");
        verify(categoryRepository).save(category);
        verify(categoryDtoConverter).convert(category);
    }

    @Test
    void testDeleteCategoryById_whenIdNotExist_shouldThrowNotFoundException() {

        when(categoryRepository.findById("id")).thenThrow(GeneralNotFoundException.class);

        assertThrows(GeneralNotFoundException.class, () -> categoryService.getCategoryById("id"));

        verify(categoryRepository).findById("id");
        verifyNoInteractions(categoryDtoConverter);
    }

    @Test
    void testDeleteCategoryById_whenIdExist() {

        categoryService.deleteCategoryById("id");

        verify(categoryRepository).deleteById("id");
    }

    @Test
    void testGetCategories_whenIdListExist_shouldReturnCategoryList() {

        List<Category> categoryList = generateCategories();
        List<CategoryDto> categoryDtoList = generateCategoryDtos();
        List<String> idList = new ArrayList<>();
        idList.add("id");

        when(categoryRepository.findAllByIdIn(idList)).thenReturn(categoryList);
        when(categoryDtoConverter.convertToCategoryDtos(categoryList)).thenReturn(categoryDtoList);

        List<Category> result = categoryService.getCategories(idList);

        assertEquals(categoryList, result);

        verify(categoryRepository).findAllByIdIn(idList);
        verifyNoInteractions(categoryDtoConverter);
    }

    @Test
    void testGetCategories_whenIdListNotExist_shouldThrowNotFoundException() {

        List<String> idList = List.of("id");
        when(categoryRepository.findAllByIdIn(idList)).thenThrow(GeneralNotFoundException.class);

        assertThrows(GeneralNotFoundException.class, () -> categoryService.getCategories(idList));

        verify(categoryRepository).findAllByIdIn(idList);
        verifyNoInteractions(categoryDtoConverter);
    }

    @AfterEach
    void tearDown() {
    }
}