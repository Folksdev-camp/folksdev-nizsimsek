package com.nizsimsek.blogApp.controller;

import com.nizsimsek.blogApp.IntegrationTestSupport;
import com.nizsimsek.blogApp.dto.request.CreateCategoryReq;
import com.nizsimsek.blogApp.dto.request.UpdateCategoryReq;
import com.nizsimsek.blogApp.model.Category;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

class CategoryControllerIT extends IntegrationTestSupport {
    
    @Test
    void testGetCategorys_shouldReturnEmptyList() throws Exception {

        this.mockMvc.perform(get("/v1/categories/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<Category> categoryList = categoryRepository.findAll();

        assertEquals(0, categoryList.size());
    }

    @Test
    void testGetCategories_shouldReturnCategoryDtoList() throws Exception {

        categoryRepository.save(generateCategory(1));

        this.mockMvc.perform(get("/v1/categories/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<Category> categoryList = categoryRepository.findAll();

        assertEquals(1, categoryList.size());
    }

    @Test
    void testGetCategoryById_whenCategoryIdNotExist_shouldReturnGeneralNotFoundException() throws Exception {

        this.mockMvc.perform(get("/v1/categories/not-exist-id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetCategoryById_whenCategoryIdExist_shouldReturnCategoryDto() throws Exception {

        Category category = categoryRepository.save(generateCategory(1));

        this.mockMvc.perform(get("/v1/categories/" + category.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        Category getCategory = categoryRepository.findById(Objects.requireNonNull(category.getId())).orElse(null);

        assertEquals(category, getCategory);
    }

    @Test
    void testCreateCategory_whenCreateCategoryReqInValid_shouldReturn400BadRequest() throws Exception {

        CreateCategoryReq createCategoryReq = new CreateCategoryReq(
                ""
        );

        this.mockMvc.perform(post("/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(createCategoryReq)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", notNullValue()));
    }

    @Test
    void testCreateCategory_whenCreateCategoryReqValid_shouldReturnCategoryDto() throws Exception {

        CreateCategoryReq createCategoryReq = generateCreateCategoryReq();

        this.mockMvc.perform(post("/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(createCategoryReq)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("name")));

        List<Category> categoryList = categoryRepository.findAll();

        assertEquals(1, categoryList.size());
    }

    @Test
    void testUpdateCategory_whenUpdateCategoryReqInValid_shouldReturn400BadRequest() throws Exception {

        UpdateCategoryReq updateCategoryReq = new UpdateCategoryReq(
                ""
        );

        this.mockMvc.perform(put("/v1/categories/not-exist-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(updateCategoryReq)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", notNullValue()));
    }

    @Test
    void testUpdateCategory_whenUpdateCategoryReqValid_shouldReturnCategoryDto() throws Exception {

        UpdateCategoryReq updateCategoryReq = new UpdateCategoryReq(
                "name"
        );
        Category category = categoryRepository.save(generateCategory());

        this.mockMvc.perform(put("/v1/categories/" + category.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(updateCategoryReq)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(category.getName())));

        Category updatedCategory = categoryRepository.findById(Objects.requireNonNull(category.getId())).orElse(null);

        assertEquals(updatedCategory, category);
    }

    @Test
    void testDeleteCategoryById_whenCategoryIdExist_shouldReturnString() throws Exception {

        Category category = categoryRepository.save(generateCategory());

        this.mockMvc.perform(delete("/v1/categories/" + category.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string("Category deleted.."));
    }

    @AfterEach
    void tearDown() {
        categoryRepository.deleteAll();
    }

}