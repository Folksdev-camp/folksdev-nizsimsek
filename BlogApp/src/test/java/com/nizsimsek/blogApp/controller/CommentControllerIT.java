package com.nizsimsek.blogApp.controller;

import com.nizsimsek.blogApp.IntegrationTestSupport;
import com.nizsimsek.blogApp.dto.request.CreateCommentReq;
import com.nizsimsek.blogApp.dto.request.UpdateCommentReq;
import com.nizsimsek.blogApp.model.Category;
import com.nizsimsek.blogApp.model.Comment;
import com.nizsimsek.blogApp.model.Post;
import com.nizsimsek.blogApp.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class CommentControllerIT extends IntegrationTestSupport {

    @Test
    void testGetComments_shouldReturnEmptyList() throws Exception {

        this.mockMvc.perform(get("/v1/comments/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<Comment> commentList = commentRepository.findAll();

        assertEquals(0, commentList.size());
    }

    @Test
    void testGetComments_shouldReturnCommentDtoList() throws Exception {

        List<Comment> commentList = generateComments();

        this.mockMvc.perform(get("/v1/comments/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        assertEquals(1, commentList.size());
    }

    @Test
    void testGetCommentById_whenCommentIdNotExist_shouldReturnGeneralNotFoundException() throws Exception {

        this.mockMvc.perform(get("/v1/comments/not-exist-id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetCommentById_whenCommentIdExist_shouldReturnCommentDto() throws Exception {

        List<Category> categoryList = categoryRepository.saveAll(generateCategoryList(2));
        User user = userRepository.save(new User("username", "email", "firstName", "lastName", "password"));
        Post post = postRepository.save(new Post("title", "content", categoryList, user));

        Comment comment = new Comment(
                "content",
                user,
                post
        );
        comment = commentRepository.save(comment);

        this.mockMvc.perform(get("/v1/comments/" + comment.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        Comment getComment = commentRepository.findById(Objects.requireNonNull(comment.getId())).orElse(null);

        assertEquals(comment, getComment);
    }

    @Test
    void testCreateComment_whenCreateCommentReqInValid_shouldReturn400BadRequest() throws Exception {

        CreateCommentReq createCommentReq = new CreateCommentReq(
                "",
                "",
                ""
        );

        this.mockMvc.perform(post("/v1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(createCommentReq)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", notNullValue()))
                .andExpect(jsonPath("$.authorId", notNullValue()))
                .andExpect(jsonPath("$.postId", notNullValue()));
    }

    @Test
    void testCreateComment_whenCreateCommentReqValid_shouldReturnCommentDto() throws Exception {

        List<Category> categoryList = categoryRepository.saveAll(generateCategoryList(2));
        User user = userRepository.save(new User("username", "email", "firstName", "lastName", "password"));
        Post post = postRepository.save(new Post("title", "content", categoryList, user));

        CreateCommentReq createCommentReq = new CreateCommentReq(
                "content",
                Objects.requireNonNull(user.getId()),
                Objects.requireNonNull(post.getId())
        );

        this.mockMvc.perform(post("/v1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(createCommentReq)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", notNullValue()))
                .andExpect(jsonPath("$.author", notNullValue()))
                .andExpect(jsonPath("$.post", notNullValue()));

        List<Comment> commentList = commentRepository.findAll();

        assertEquals(1, commentList.size());
    }

    @Test
    void testUpdateComment_whenUpdateCommentReqInValid_shouldReturn400BadRequest() throws Exception {

        UpdateCommentReq updateCommentReq = new UpdateCommentReq(
                ""
        );

        this.mockMvc.perform(put("/v1/comments/not-exist-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(updateCommentReq)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", notNullValue()));
    }

    @Test
    void testUpdateComment_whenUpdateCommentReqValid_shouldReturnCommentDto() throws Exception {

        List<Category> categoryList = categoryRepository.saveAll(generateCategoryList(2));
        User user = userRepository.save(new User("username", "email", "firstName", "lastName", "password"));
        Post post = postRepository.save(new Post("title", "content", categoryList, user));

        UpdateCommentReq updateCommentReq = new UpdateCommentReq(
                "content"
        );
        Comment comment = new Comment(
                "content",
                user,
                post
        );
        comment = commentRepository.save(comment);

        this.mockMvc.perform(put("/v1/comments/" + comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(updateCommentReq)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", is(comment.getContent())))
                .andExpect(jsonPath("$.author", notNullValue()));

        Comment updatedComment = commentRepository.findById(Objects.requireNonNull(comment.getId())).orElse(null);

        assertEquals(updatedComment, comment);
    }

    @Test
    void testDeleteCommentById_whenCommentIdExist_shouldReturnString() throws Exception {

        List<Category> categoryList = categoryRepository.saveAll(generateCategoryList(2));
        User user = userRepository.save(new User("username", "email", "firstName", "lastName", "password"));
        Post post = postRepository.save(new Post("title", "content", categoryList, user));

        Comment comment = new Comment(
                "content",
                user,
                post
        );
        comment = commentRepository.save(comment);

        this.mockMvc.perform(delete("/v1/comments/" + comment.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string("Comment deleted.."));
    }

    @Test
    void testLikeCommentById_whenIdNotExist_shouldThrowGeneralNotFoundException() throws Exception {

        UpdateCommentReq updateCommentReq = new UpdateCommentReq(
                ""
        );

        this.mockMvc.perform(patch("/v1/comments/like/not-exist-id")
                        .contentType(MediaType.valueOf("text/plain;charset=UTF-8"))
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(updateCommentReq)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")));
    }

    @Test
    void testLikeCommentById_whenIdExist_shouldUpdateCommentAndReturnCommentDto() throws Exception {

        User user = userRepository.save(new User(
                "username",
                "email",
                "firstName",
                "lastName",
                "password"
        ));
        List<Category> categoryList = categoryRepository.saveAll(generateCategoryList(2));
        Post post = new Post(
                "title",
                "content",
                categoryList,
                user
        );
        post = postRepository.save(post);
        user = new User(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPassword(),
                generatePosts()
        );
        Comment comment = new Comment(
                "content",
                user,
                post
        );
        comment = commentRepository.save(comment);
        assert comment.getLikes() >= 0;
        Comment updatedComment = new Comment(
                comment.getId(),
                comment.getContent(),
                comment.getLikes() + 1,
                comment.getDislikes(),
                user,
                post
        );

        this.mockMvc.perform(patch("/v1/comments/like/" + comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(updatedComment)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", is(updatedComment.getContent())));

        Comment updatedCommentInDb = commentRepository.findById(Objects.requireNonNull(comment.getId())).orElse(null);

        assertEquals(updatedComment, updatedCommentInDb);
    }

    @Test
    void testunlikeCommentById_whenIdNotExist_shouldThrowGeneralNotFoundException() throws Exception {

        UpdateCommentReq updateCommentReq = new UpdateCommentReq(
                ""
        );

        this.mockMvc.perform(patch("/v1/comments/unlike/not-exist-id")
                        .contentType(MediaType.valueOf("text/plain;charset=UTF-8"))
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(updateCommentReq)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")));
    }

    @Test
    void testunlikeCommentById_whenIdExist_shouldUpdateCommentAndReturnCommentDto() throws Exception {

        User user = userRepository.save(new User(
                "username",
                "email",
                "firstName",
                "lastName",
                "password"
        ));
        List<Category> categoryList = categoryRepository.saveAll(generateCategoryList(2));
        Post post = new Post(
                "title",
                "content",
                categoryList,
                user
        );
        post = postRepository.save(post);
        user = new User(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPassword(),
                generatePosts()
        );
        Comment comment = new Comment(
                "content",
                user,
                post
        );
        comment = commentRepository.save(comment);
        Comment updatedComment = new Comment(
                comment.getId(),
                comment.getContent(),
                comment.getLikes() - 1,
                comment.getDislikes(),
                user,
                post
        );

        this.mockMvc.perform(patch("/v1/comments/unlike/" + comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(updatedComment)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", is(updatedComment.getContent())));

        Comment updatedCommentInDb = commentRepository.findById(Objects.requireNonNull(comment.getId())).orElse(null);

        assertEquals(updatedComment, updatedCommentInDb);
    }

    @Test
    void testdislikeCommentById_whenIdNotExist_shouldThrowGeneralNotFoundException() throws Exception {

        UpdateCommentReq updateCommentReq = new UpdateCommentReq(
                ""
        );

        this.mockMvc.perform(patch("/v1/comments/dislike/not-exist-id")
                        .contentType(MediaType.valueOf("text/plain;charset=UTF-8"))
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(updateCommentReq)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")));
    }

    @Test
    void testdislikeCommentById_whenIdExist_shouldUpdateCommentAndReturnCommentDto() throws Exception {

        User user = userRepository.save(new User(
                "username",
                "email",
                "firstName",
                "lastName",
                "password"
        ));
        List<Category> categoryList = categoryRepository.saveAll(generateCategoryList(2));
        Post post = new Post(
                "title",
                "content",
                categoryList,
                user
        );
        post = postRepository.save(post);
        user = new User(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPassword(),
                generatePosts()
        );
        Comment comment = new Comment(
                "content",
                user,
                post
        );
        comment = commentRepository.save(comment);
        assert comment.getLikes() >= 0;
        Comment updatedComment = new Comment(
                comment.getId(),
                comment.getContent(),
                comment.getLikes(),
                comment.getDislikes() + 1,
                user,
                post
        );

        this.mockMvc.perform(patch("/v1/comments/dislike/" + comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(updatedComment)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", is(updatedComment.getContent())));

        Comment updatedCommentInDb = commentRepository.findById(Objects.requireNonNull(comment.getId())).orElse(null);

        assertEquals(updatedComment, updatedCommentInDb);
    }

    @Test
    void testundislikeCommentById_whenIdNotExist_shouldThrowGeneralNotFoundException() throws Exception {

        UpdateCommentReq updateCommentReq = new UpdateCommentReq(
                ""
        );

        this.mockMvc.perform(patch("/v1/comments/undislike/not-exist-id")
                        .contentType(MediaType.valueOf("text/plain;charset=UTF-8"))
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(updateCommentReq)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")));
    }

    @Test
    void testundislikeCommentById_whenIdExist_shouldUpdateCommentAndReturnCommentDto() throws Exception {

        User user = userRepository.save(new User(
                "username",
                "email",
                "firstName",
                "lastName",
                "password"
        ));
        List<Category> categoryList = categoryRepository.saveAll(generateCategoryList(2));
        Post post = new Post(
                "title",
                "content",
                categoryList,
                user
        );
        post = postRepository.save(post);
        user = new User(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPassword(),
                generatePosts()
        );
        Comment comment = new Comment(
                "content",
                user,
                post
        );
        comment = commentRepository.save(comment);
        assert comment.getLikes() >= 0;
        Comment updatedComment = new Comment(
                comment.getId(),
                comment.getContent(),
                comment.getLikes(),
                comment.getDislikes() - 1,
                user,
                post
        );

        this.mockMvc.perform(patch("/v1/comments/undislike/" + comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(updatedComment)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", is(updatedComment.getContent())));

        Comment updatedCommentInDb = commentRepository.findById(Objects.requireNonNull(comment.getId())).orElse(null);

        assertEquals(updatedComment, updatedCommentInDb);
    }

    @AfterEach
    void tearDown() {
        commentRepository.deleteAll();
    }
}