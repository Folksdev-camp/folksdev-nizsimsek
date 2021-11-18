package com.nizsimsek.blogApp;

import com.nizsimsek.blogApp.dto.*;
import com.nizsimsek.blogApp.dto.request.*;
import com.nizsimsek.blogApp.model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TestSupport {

    LocalDateTime date = LocalDateTime.of(2021,11,11,11,11);

    // USER TEST SUPPORT
    public User generateUser() {

        return new User(
                "username",
                "email",
                "firstName",
                "lastName",
                "password"
        );
    }

    public UserDto generateUserDto() {

        return new UserDto(
                "id",
                "username",
                "email",
                "firstName",
                "lastName",
                "password"
        );
    }

    public List<User> generateUsers() {

        List<User> users = new ArrayList<>();
        User user = generateUser();
        users.add(user);
        return users;
    }

    public List<UserDto> generateUserDtos() {

        List<UserDto> users = new ArrayList<>();
        UserDto user = generateUserDto();
        users.add(user);
        return users;
    }

    public CreateUserReq generateCreateUserReq() {

        return new CreateUserReq(
                "username",
                "email",
                "firstName",
                "lastName",
                "password"
        );
    }

    public UpdateUserReq generateUpdateUserReq() {

        return new UpdateUserReq(
                "username",
                "email",
                "firstName",
                "lastName",
                "password"
        );
    }

    // CATEGORY TEST SUPPORT
    public Category generateCategory() {

        return new Category(
                "name"
        );
    }

    public CategoryDto generateCategoryDto() {

        return new CategoryDto(
                "id",
                "name"
        );
    }

    public List<Category> generateCategories() {

        List<Category> categories = new ArrayList<>();
        Category category = new Category(
                "id",
                "name"
        );
        categories.add(category);
        return categories;
    }

    public List<CategoryDto> generateCategoryDtos() {

        List<CategoryDto> categoryDtos = new ArrayList<>();
        CategoryDto categoryDto = generateCategoryDto();
        categoryDtos.add(categoryDto);
        return categoryDtos;
    }

    public CreateCategoryReq generateCreateCategoryReq() {

        return new CreateCategoryReq(
                "name"
        );
    }

    public UpdateCategoryReq generateUpdateCategoryReq() {

        return new UpdateCategoryReq(
                "name"
        );
    }

    // POST TEST SUPPORT
    public Post generatePost() {

        User user = new User(
                "id",
                "username",
                "email",
                "firstName",
                "lastName",
                "password"
        );
        List<Category> categories = new ArrayList<>();
        Category category = new Category(
                "name"
        );
        categories.add(category);
        return new Post(
                "title",
                "content",
                categories,
                user
        );
    }

    public Post generatePostAllFields() {

        User user = new User(
                "id",
                "username",
                "email",
                "firstName",
                "lastName",
                "password"
        );
        List<Category> categories = new ArrayList<>();
        Category category = new Category(
                "name"
        );
        categories.add(category);
        return new Post(
                "id",
                "title",
                "content",
                10,
                10,
                categories,
                user
        );
    }

    public PostDto generatePostDto() {

        UserDto userDto = new UserDto(
                "id",
                "username",
                "email",
                "firstName",
                "lastName",
                "password"
        );
        List<CategoryDto> categoryDtos = generateCategoryDtos();

        return new PostDto(
                "id",
                "title",
                "content",
                10,
                10,
                date,
                date,
                categoryDtos,
                userDto
        );
    }

    public List<Post> generatePosts() {

        List<Post> posts = new ArrayList<>();
        Post post = generatePost();
        posts.add(post);
        return posts;
    }

    public List<PostDto> generatePostDtos() {

        List<PostDto> postDtos = new ArrayList<>();
        PostDto postDto = generatePostDto();
        postDtos.add(postDto);
        return postDtos;
    }

    public CreatePostReq generateCreatePostReq() {

        List<String> categoryIds = new ArrayList<>();
        Category category = new Category(
                "id",
                "name"
        );
        User user = new User(
                "id",
                "username",
                "email",
                "firstName",
                "lastName",
                "password"
        );
        categoryIds.add(category.getId());

        return new CreatePostReq(
                "title",
                "content",
                Objects.requireNonNull(user.getId()),
                categoryIds
        );
    }

    public UpdatePostReq generateUpdatePostReq() {

        List<String> categoryIds = new ArrayList<>();
        Category category = generateCategory();
        categoryIds.add(category.getId());

        return new UpdatePostReq(
                "title",
                "content",
                categoryIds
        );
    }

    // COMMENT TEST SUPPORT
    public Comment generateComment() {

        User user = generateUser();
        Post post = new Post(
                "title",
                "content",
                List.of(),
                user
        );

        return new Comment(
                "content",
                user,
                post
        );
    }

    public Comment generateCommentAllFields() {

        User user = new User(
                "id",
                "username",
                "email",
                "firstName",
                "lastName",
                "password"
        );
        Post post = new Post(
                "title",
                "content",
                List.of(),
                user
        );
        return new Comment(
                "id",
                "content",
                10,
                10,
                user,
                post
        );
    }

    public CommentDto generateCommentDto() {

        UserDto userDto = new UserDto(
                "id",
                "username",
                "email",
                "firstName",
                "lastName",
                "password"
        );
        PostDto postDto = new PostDto(
                "id",
                "title",
                "content",
                10,
                10,
                date,
                date,
                List.of(),
                userDto
        );
        return new CommentDto(
                "id",
                "content",
                10,
                10,
                date,
                date,
                userDto,
                postDto
        );
    }

    public List<Comment> generateComments() {

        List<Comment> comments = new ArrayList<>();
        Comment comment = generateComment();
        comments.add(comment);
        return comments;
    }

    public List<CommentDto> generateCommentDtos() {

        List<CommentDto> commentDtos = new ArrayList<>();
        CommentDto commentDto = new CommentDto(
                "id",
                "content",
                10,
                10,
                date
        );
        commentDtos.add(commentDto);
        return commentDtos;
    }

    public CreateCommentReq generateCreateCommentReq() {
        User user = new User(
                "id",
                "username",
                "email",
                "firstName",
                "lastName",
                "password"
        );
        Post post = new Post(
                "id",
                "title",
                "content",
                List.of(generateCategory()),
                user
        );

        return new CreateCommentReq(
                "content",
                Objects.requireNonNull(user.getId()),
                Objects.requireNonNull(post.getId())
        );
    }

    public UpdateCommentReq generateUpdateCommentReq() {

        return new UpdateCommentReq(
                "content"
        );
    }

    // SUBCOMMENT TEST SUPPORT
    public SubComment generateSubComment() {

        User user = generateUser();
        Comment comment = generateComment();

        return new SubComment(
                "content",
                user,
                comment
        );
    }

    public SubComment generateSubCommentAllFields() {

        User user = new User(
                "id",
                "username",
                "email",
                "firstName",
                "lastName",
                "password"
        );
        Comment comment = new Comment(
                "content",
                user,
                generatePost()
        );
        return new SubComment(
                "id",
                "content",
                10,
                10,
                user,
                comment
        );
    }

    public SubCommentDto generateSubCommentDto() {

        UserDto userDto = generateUserDto();
        CommentDto commentDto = generateCommentDto();

        return new SubCommentDto(
                "id",
                "content",
                10,
                10,
                date,
                date,
                userDto,
                commentDto
        );
    }

    public List<SubComment> generateSubComments() {

        List<SubComment> subComments = new ArrayList<>();
        SubComment subComment = generateSubComment();
        subComments.add(subComment);
        return subComments;
    }

    public List<SubCommentDto> generateSubCommentDtos() {

        List<SubCommentDto> subCommentDtos = new ArrayList<>();
        SubCommentDto subCommentDto = generateSubCommentDto();
        subCommentDtos.add(subCommentDto);
        return subCommentDtos;
    }

    public CreateSubCommentReq generateCreateSubCommentReq() {

        return new CreateSubCommentReq(
                "content",
                "id",
                "id"
        );
    }

    public UpdateSubCommentReq generateUpdateSubCommentReq() {

        return new UpdateSubCommentReq(
                "content"
        );
    }

}
