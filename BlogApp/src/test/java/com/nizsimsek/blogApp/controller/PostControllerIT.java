package com.nizsimsek.blogApp.controller;

import com.nizsimsek.blogApp.IntegrationTestSupport;
import com.nizsimsek.blogApp.dto.request.CreatePostReq;
import com.nizsimsek.blogApp.dto.request.UpdatePostReq;
import com.nizsimsek.blogApp.model.Category;
import com.nizsimsek.blogApp.model.Post;
import com.nizsimsek.blogApp.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

class PostControllerIT extends IntegrationTestSupport {

    @Test
    void testGetPosts_shouldReturnEmptyList() throws Exception {

        this.mockMvc.perform(get("/v1/posts/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<Post> postList = postRepository.findAll();

        assertEquals(0, postList.size());
    }

    @Test
    void testGetPosts_shouldReturnPostDtoList() throws Exception {

        List<Post> postList = generatePosts();

        this.mockMvc.perform(get("/v1/posts/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        assertEquals(1, postList.size());
    }

    @Test
    void testGetPostById_whenPostIdNotExist_shouldReturnGeneralNotFoundException() throws Exception {

        this.mockMvc.perform(get("/v1/posts/not-exist-id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetPostById_whenPostIdExist_shouldReturnPostDto() throws Exception {

        User user = userRepository.save(generateUser());
        List<Category> categoryList = categoryRepository.saveAll(generateCategories());

        Post post = new Post(
                "title",
                "content",
                categoryList,
                user
        );
        post = postRepository.save(post);

        this.mockMvc.perform(get("/v1/posts/" + post.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        Post getPost = postRepository.findById(Objects.requireNonNull(post.getId())).orElse(null);

        assertEquals(post, getPost);
    }

    @Test
    void testCreatePost_whenCreatePostReqInValid_shouldReturn400BadRequest() throws Exception {

        List<String> categoryIds = List.of("");
        CreatePostReq createPostReq = new CreatePostReq(
                "",
                "",
                "",
                categoryIds
        );

        this.mockMvc.perform(post("/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(createPostReq)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", notNullValue()))
                .andExpect(jsonPath("$.content", notNullValue()))
                .andExpect(jsonPath("$.authorId", notNullValue()));
    }

    @Test
    void testCreatePost_whenCreatePostReqValid_shouldCreatePostAndReturnPostDto() throws Exception {

        User user = userRepository.save(generateUser());
        List<Category> categoryList = categoryRepository.saveAll(generateCategoryList(2));
        List<String> categoryIds = categoryList.stream().map(Category::getId).collect(Collectors.toList());
        CreatePostReq createPostReq = new CreatePostReq(
                "title",
                "content",
                Objects.requireNonNull(user.getId()),
                categoryIds
        );

        this.mockMvc.perform(post("/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(createPostReq)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", notNullValue()))
                .andExpect(jsonPath("$.content", notNullValue()))
                .andExpect(jsonPath("$.author", notNullValue()));

        List<Post> postList = postRepository.findAll();

        assertEquals(1, postList.size());
    }

    @Test
    void testUpdatePost_whenUpdatePostReqInValid_shouldReturn400BadRequest() throws Exception {

        UpdatePostReq updatePostReq = new UpdatePostReq(
                "",
                "",
                List.of()
        );

        this.mockMvc.perform(put("/v1/posts/not-exist-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(updatePostReq)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", notNullValue()))
                .andExpect(jsonPath("$.content", notNullValue()));
    }

    @Test
    void testUpdatePost_whenUpdatePostReqValid_shouldUpdatePostAndReturnPostDto() throws Exception {

        User user = userRepository.save(generateUser());
        List<Category> categoryList = categoryRepository.saveAll(generateCategoryList(2));
        List<String> categoryIds = categoryList.stream().map(Category::getId).collect(Collectors.toList());
        UpdatePostReq updatePostReq = new UpdatePostReq(
                "title",
                "content",
                categoryIds
        );
        Post post = new Post(
                "title",
                "content",
                categoryList,
                user
        );
        post = postRepository.save(post);

        this.mockMvc.perform(put("/v1/posts/" + post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(updatePostReq)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is(post.getTitle())))
                .andExpect(jsonPath("$.content", is(post.getContent())))
                .andExpect(jsonPath("$.author", notNullValue()));

        Post updatedPost = postRepository.findById(Objects.requireNonNull(post.getId())).orElse(null);

        assertEquals(updatedPost, post);
    }

    @Test
    void testDeletePostById_whenPostIdExist_shouldDeleteUserAndReturnString() throws Exception {

        List<Category> categoryList = categoryRepository.saveAll(generateCategoryList(2));
        User user = userRepository.save(generateUser());
        Post post = new Post(
                "title",
                "content",
                categoryList,
                user
        );
        post = postRepository.save(post);

        this.mockMvc.perform(delete("/v1/posts/" + post.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string("Post deleted.."));
    }

    @Test
    void testLikePostById_whenIdNotExist_shouldThrowGeneralNotFoundException() throws Exception {

        UpdatePostReq updatePostReq = new UpdatePostReq(
                "",
                "",
                List.of()
        );

        this.mockMvc.perform(patch("/v1/posts/like/not-exist-id")
                        .contentType(MediaType.valueOf("text/plain;charset=UTF-8"))
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(updatePostReq)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")));
    }

    @Test
    void testLikePostById_whenIdExist_shouldUpdatePostAndReturnPostDto() throws Exception {

        User user = userRepository.save(generateUser(1));
        List<Category> categoryList = categoryRepository.saveAll(generateCategoryList(2));
        Post post = new Post(
                "title",
                "content",
                categoryList,
                user
        );
        post = postRepository.save(post);
        assert post.getLikes() >= 0;
        Post updatedPost = new Post(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getLikes() + 1,
                post.getDislikes(),
                categoryList,
                user
        );

        this.mockMvc.perform(patch("/v1/posts/like/" + post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(updatedPost)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is(post.getTitle())))
                .andExpect(jsonPath("$.content", is(post.getContent())))
                .andExpect(jsonPath("$.author", notNullValue()));

        Post updatedPostInDb = postRepository.findById(Objects.requireNonNull(post.getId())).orElse(null);

        assertEquals(updatedPost, updatedPostInDb);
    }

    @Test
    void testunlikePostById_whenIdNotExist_shouldThrowGeneralNotFoundException() throws Exception {

        UpdatePostReq updatePostReq = new UpdatePostReq(
                "",
                "",
                List.of()
        );

        this.mockMvc.perform(patch("/v1/posts/unlike/not-exist-id")
                        .contentType(MediaType.valueOf("text/plain;charset=UTF-8"))
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(updatePostReq)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")));
    }

    @Test
    void testunlikePostById_whenIdExist_shouldUpdatePostAndReturnPostDto() throws Exception {

        User user = userRepository.save(generateUser(1));
        List<Category> categoryList = categoryRepository.saveAll(generateCategoryList(2));
        Post post = new Post(
                "title",
                "content",
                categoryList,
                user
        );
        post = postRepository.save(post);
        Post updatedPost = new Post(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getLikes() - 1,
                post.getDislikes(),
                categoryList,
                user
        );

        this.mockMvc.perform(patch("/v1/posts/unlike/" + post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(updatedPost)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is(post.getTitle())))
                .andExpect(jsonPath("$.content", is(post.getContent())))
                .andExpect(jsonPath("$.author", notNullValue()));

        Post updatedPostInDb = postRepository.findById(Objects.requireNonNull(post.getId())).orElse(null);

        assertEquals(updatedPost, updatedPostInDb);
    }

    @Test
    void testdislikePostById_whenIdNotExist_shouldThrowGeneralNotFoundException() throws Exception {

        UpdatePostReq updatePostReq = new UpdatePostReq(
                "",
                "",
                List.of()
        );

        this.mockMvc.perform(patch("/v1/posts/dislike/not-exist-id")
                        .contentType(MediaType.valueOf("text/plain;charset=UTF-8"))
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(updatePostReq)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")));
    }

    @Test
    void testdislikePostById_whenIdExist_shouldUpdatePostAndReturnPostDto() throws Exception {

        User user = userRepository.save(generateUser(1));
        List<Category> categoryList = categoryRepository.saveAll(generateCategoryList(2));
        Post post = new Post(
                "title",
                "content",
                categoryList,
                user
        );
        post = postRepository.save(post);
        assert post.getLikes() >= 0;
        Post updatedPost = new Post(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getLikes(),
                post.getDislikes() + 1,
                categoryList,
                user
        );

        this.mockMvc.perform(patch("/v1/posts/dislike/" + post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(updatedPost)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is(post.getTitle())))
                .andExpect(jsonPath("$.content", is(post.getContent())))
                .andExpect(jsonPath("$.author", notNullValue()));

        Post updatedPostInDb = postRepository.findById(Objects.requireNonNull(post.getId())).orElse(null);

        assertEquals(updatedPost, updatedPostInDb);
    }

    @Test
    void testundislikePostById_whenIdNotExist_shouldThrowGeneralNotFoundException() throws Exception {

        UpdatePostReq updatePostReq = new UpdatePostReq(
                "",
                "",
                List.of()
        );

        this.mockMvc.perform(patch("/v1/posts/undislike/not-exist-id")
                        .contentType(MediaType.valueOf("text/plain;charset=UTF-8"))
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(updatePostReq)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")));
    }

    @Test
    void testundislikePostById_whenIdExist_shouldUpdatePostAndReturnPostDto() throws Exception {

        User user = userRepository.save(generateUser(1));
        List<Category> categoryList = categoryRepository.saveAll(generateCategoryList(2));
        Post post = new Post(
                "title",
                "content",
                categoryList,
                user
        );
        post = postRepository.save(post);
        Post updatedPost = new Post(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getLikes(),
                post.getDislikes() - 1,
                categoryList,
                user
        );

        this.mockMvc.perform(patch("/v1/posts/undislike/" + post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(updatedPost)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is(post.getTitle())))
                .andExpect(jsonPath("$.content", is(post.getContent())))
                .andExpect(jsonPath("$.author", notNullValue()));

        Post updatedPostInDb = postRepository.findById(Objects.requireNonNull(post.getId())).orElse(null);

        assertEquals(updatedPost, updatedPostInDb);
    }

    @AfterEach
    void tearDown() {
        postRepository.deleteAll();
    }
}