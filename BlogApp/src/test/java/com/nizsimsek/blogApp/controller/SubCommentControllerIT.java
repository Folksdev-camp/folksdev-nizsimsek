package com.nizsimsek.blogApp.controller;

import com.nizsimsek.blogApp.IntegrationTestSupport;
import com.nizsimsek.blogApp.dto.request.CreateSubCommentReq;
import com.nizsimsek.blogApp.dto.request.UpdateSubCommentReq;
import com.nizsimsek.blogApp.model.*;
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

class SubCommentControllerIT extends IntegrationTestSupport {

    @Test
    void testGetSubComments_shouldReturnEmptyList() throws Exception {

        this.mockMvc.perform(get("/v1/subcomments/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<SubComment> subCommentList = subCommentRepository.findAll();

        assertEquals(0, subCommentList.size());
    }

    @Test
    void testGetSubComments_shouldReturnSubCommentDtoList() throws Exception {

        List<SubComment> subCommentList = generateSubCommentList(1);

        this.mockMvc.perform(get("/v1/subcomments/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        assertEquals(1, subCommentList.size());
    }

    @Test
    void testGetSubCommentById_whenSubCommentIdNotExist_shouldReturnGeneralNotFoundException() throws Exception {

        this.mockMvc.perform(get("/v1/subcomments/not-exist-id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetSubCommentById_whenSubCommentIdExist_shouldReturnSubCommentDto() throws Exception {

        List<Category> categoryList = categoryRepository.saveAll(generateCategoryList(2));
        User user = userRepository.save(new User("username", "email", "firstName", "lastName", "password"));
        Post post = postRepository.save(new Post("title", "content", categoryList, user));
        Comment comment = commentRepository.save(new Comment("content", user, post));

        SubComment subComment = new SubComment(
                "content",
                user,
                comment
        );
        subComment = subCommentRepository.save(subComment);

        this.mockMvc.perform(get("/v1/subcomments/" + subComment.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        SubComment getSubComment = subCommentRepository.findById(Objects.requireNonNull(subComment.getId())).orElse(null);

        assertEquals(subComment, getSubComment);
    }

    @Test
    void testCreateSubComment_whenCreateSubCommentReqInValid_shouldReturn400BadRequest() throws Exception {

        CreateSubCommentReq createSubCommentReq = new CreateSubCommentReq(
                "",
                "",
                ""
        );

        this.mockMvc.perform(post("/v1/subcomments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(createSubCommentReq)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", notNullValue()))
                .andExpect(jsonPath("$.authorId", notNullValue()))
                .andExpect(jsonPath("$.commentId", notNullValue()));
    }

    @Test
    void testCreateSubComment_whenCreateSubCommentReqValid_shouldReturnSubCommentDto() throws Exception {

        List<Category> categoryList = categoryRepository.saveAll(generateCategoryList(2));
        User user = userRepository.save(new User("username", "email", "firstName", "lastName", "password"));
        Post post = postRepository.save(new Post("title", "content", categoryList, user));
        Comment comment = commentRepository.save(new Comment("content", user, post));

        CreateSubCommentReq createSubCommentReq = new CreateSubCommentReq(
                "content",
                Objects.requireNonNull(user.getId()),
                Objects.requireNonNull(comment.getId())
        );

        this.mockMvc.perform(post("/v1/subcomments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(createSubCommentReq)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", notNullValue()))
                .andExpect(jsonPath("$.author", notNullValue()))
                .andExpect(jsonPath("$.comment", notNullValue()));

        List<SubComment> subCommentList = subCommentRepository.findAll();

        assertEquals(1, subCommentList.size());
    }

    @Test
    void testUpdateSubComment_whenUpdateSubCommentReqInValid_shouldReturn400BadRequest() throws Exception {

        UpdateSubCommentReq updateSubCommentReq = new UpdateSubCommentReq(
                ""
        );

        this.mockMvc.perform(put("/v1/subcomments/not-exist-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(updateSubCommentReq)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", notNullValue()));
    }

    @Test
    void testUpdateSubComment_whenUpdateSubCommentReqValid_shouldReturnSubCommentDto() throws Exception {

        List<Category> categoryList = categoryRepository.saveAll(generateCategoryList(2));
        User user = userRepository.save(new User("username", "email", "firstName", "lastName", "password"));
        Post post = postRepository.save(new Post("title", "content", categoryList, user));
        Comment comment = commentRepository.save(new Comment("content", user, post));

        SubComment subComment = new SubComment(
                "content",
                user,
                comment
        );
        UpdateSubCommentReq updateSubCommentReq = new UpdateSubCommentReq(
                "content"
        );
        subComment = subCommentRepository.save(subComment);

        this.mockMvc.perform(put("/v1/subcomments/" + subComment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(updateSubCommentReq)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", is(subComment.getContent())))
                .andExpect(jsonPath("$.author", notNullValue()));

        SubComment updatedSubComment = subCommentRepository.findById(Objects.requireNonNull(subComment.getId())).orElse(null);

        assertEquals(updatedSubComment, subComment);
    }

    @Test
    void testDeleteSubCommentById_whenSubCommentIdExist_shouldReturnString() throws Exception {

        List<Category> categoryList = categoryRepository.saveAll(generateCategoryList(2));
        User user = userRepository.save(new User("username", "email", "firstName", "lastName", "password"));
        Post post = postRepository.save(new Post("title", "content", categoryList, user));
        Comment comment = commentRepository.save(new Comment("content", user, post));

        SubComment subComment = new SubComment(
                "content",
                user,
                comment
        );
        subComment = subCommentRepository.save(subComment);

        this.mockMvc.perform(delete("/v1/subcomments/" + subComment.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string("SubComment deleted.."));
    }

    @Test
    void testLikeSubCommentById_whenIdNotExist_shouldThrowGeneralNotFoundException() throws Exception {

        UpdateSubCommentReq updateSubCommentReq = new UpdateSubCommentReq(
                ""
        );

        this.mockMvc.perform(patch("/v1/subcomments/like/not-exist-id")
                        .contentType(MediaType.valueOf("text/plain;charset=UTF-8"))
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(updateSubCommentReq)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")));
    }

    @Test
    void testLikeSubCommentById_whenIdExist_shouldUpdateSubCommentAndReturnSubCommentDto() throws Exception {

        List<Category> categoryList = categoryRepository.saveAll(generateCategoryList(2));
        User user = userRepository.save(new User("username", "email", "firstName", "lastName", "password"));
        Post post = postRepository.save(new Post("title", "content", categoryList, user));
        Comment comment = commentRepository.save(new Comment("content", user, post));

        SubComment subComment = new SubComment(
                "content",
                user,
                comment
        );
        subComment = subCommentRepository.save(subComment);
        assert subComment.getLikes() >= 0;
        SubComment updatedSubComment = new SubComment(
                subComment.getId(),
                subComment.getContent(),
                post.getLikes() + 1,
                subComment.getDislikes(),
                user,
                comment
        );

        this.mockMvc.perform(patch("/v1/subcomments/like/" + subComment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(updatedSubComment)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", is(updatedSubComment.getContent())));

        SubComment updatedSubCommentInDb = subCommentRepository.findById(Objects.requireNonNull(subComment.getId())).orElse(null);

        assertEquals(updatedSubComment, updatedSubCommentInDb);
    }

    @Test
    void testunlikeSubCommentById_whenIdNotExist_shouldThrowGeneralNotFoundException() throws Exception {

        UpdateSubCommentReq updateSubCommentReq = new UpdateSubCommentReq(
                ""
        );

        this.mockMvc.perform(patch("/v1/subcomments/unlike/not-exist-id")
                        .contentType(MediaType.valueOf("text/plain;charset=UTF-8"))
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(updateSubCommentReq)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")));
    }

    @Test
    void testunlikeSubCommentById_whenIdExist_shouldUpdateSubCommentAndReturnSubCommentDto() throws Exception {

        List<Category> categoryList = categoryRepository.saveAll(generateCategoryList(2));
        User user = userRepository.save(new User("username", "email", "firstName", "lastName", "password"));
        Post post = postRepository.save(new Post("title", "content", categoryList, user));
        Comment comment = commentRepository.save(new Comment("content", user, post));

        SubComment subComment = new SubComment(
                "content",
                user,
                comment
        );
        subComment = subCommentRepository.save(subComment);
        assert subComment.getLikes() >= 0;
        SubComment updatedSubComment = new SubComment(
                subComment.getId(),
                subComment.getContent(),
                post.getLikes() - 1,
                subComment.getDislikes(),
                user,
                comment
        );

        this.mockMvc.perform(patch("/v1/subcomments/unlike/" + subComment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(updatedSubComment)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", is(updatedSubComment.getContent())));

        SubComment updatedSubCommentInDb = subCommentRepository.findById(Objects.requireNonNull(subComment.getId())).orElse(null);

        assertEquals(updatedSubComment, updatedSubCommentInDb);
    }

    @Test
    void testdislikeSubCommentById_whenIdNotExist_shouldThrowGeneralNotFoundException() throws Exception {

        UpdateSubCommentReq updateSubCommentReq = new UpdateSubCommentReq(
                ""
        );

        this.mockMvc.perform(patch("/v1/subcomments/dislike/not-exist-id")
                        .contentType(MediaType.valueOf("text/plain;charset=UTF-8"))
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(updateSubCommentReq)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")));
    }

    @Test
    void testdislikeSubCommentById_whenIdExist_shouldUpdateSubCommentAndReturnSubCommentDto() throws Exception {

        List<Category> categoryList = categoryRepository.saveAll(generateCategoryList(2));
        User user = userRepository.save(new User("username", "email", "firstName", "lastName", "password"));
        Post post = postRepository.save(new Post("title", "content", categoryList, user));
        Comment comment = commentRepository.save(new Comment("content", user, post));

        SubComment subComment = new SubComment(
                "content",
                user,
                comment
        );
        subComment = subCommentRepository.save(subComment);
        assert subComment.getLikes() >= 0;
        SubComment updatedSubComment = new SubComment(
                subComment.getId(),
                subComment.getContent(),
                post.getLikes(),
                subComment.getDislikes() + 1,
                user,
                comment
        );

        this.mockMvc.perform(patch("/v1/subcomments/dislike/" + subComment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(updatedSubComment)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", is(updatedSubComment.getContent())));

        SubComment updatedSubCommentInDb = subCommentRepository.findById(Objects.requireNonNull(subComment.getId())).orElse(null);

        assertEquals(updatedSubComment, updatedSubCommentInDb);
    }

    @Test
    void testnudislikeSubCommentById_whenIdNotExist_shouldThrowGeneralNotFoundException() throws Exception {

        UpdateSubCommentReq updateSubCommentReq = new UpdateSubCommentReq(
                ""
        );

        this.mockMvc.perform(patch("/v1/subcomments/undislike/not-exist-id")
                        .contentType(MediaType.valueOf("text/plain;charset=UTF-8"))
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(updateSubCommentReq)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")));
    }

    @Test
    void testnudislikeSubCommentById_whenIdExist_shouldUpdateSubCommentAndReturnSubCommentDto() throws Exception {

        List<Category> categoryList = categoryRepository.saveAll(generateCategoryList(2));
        User user = userRepository.save(new User("username", "email", "firstName", "lastName", "password"));
        Post post = postRepository.save(new Post("title", "content", categoryList, user));
        Comment comment = commentRepository.save(new Comment("content", user, post));

        SubComment subComment = new SubComment(
                "content",
                user,
                comment
        );
        subComment = subCommentRepository.save(subComment);
        assert subComment.getLikes() >= 0;
        SubComment updatedSubComment = new SubComment(
                subComment.getId(),
                subComment.getContent(),
                post.getLikes(),
                subComment.getDislikes() - 1,
                user,
                comment
        );

        this.mockMvc.perform(patch("/v1/subcomments/undislike/" + subComment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(updatedSubComment)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", is(updatedSubComment.getContent())));

        SubComment updatedSubCommentInDb = subCommentRepository.findById(Objects.requireNonNull(subComment.getId())).orElse(null);

        assertEquals(updatedSubComment, updatedSubCommentInDb);
    }

    @AfterEach
    void tearDown() {
        subCommentRepository.deleteAll();
    }
}