package com.nizsimsek.blogApp.dto.converter;

import com.nizsimsek.blogApp.dto.CategoryDto;
import com.nizsimsek.blogApp.dto.CommentDto;
import com.nizsimsek.blogApp.dto.PostDto;
import com.nizsimsek.blogApp.dto.SubCommentDto;
import com.nizsimsek.blogApp.model.Category;
import com.nizsimsek.blogApp.model.Comment;
import com.nizsimsek.blogApp.model.Post;
import com.nizsimsek.blogApp.model.SubComment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BaseDtoConverter {

    protected List<CategoryDto> getCategoryDtos(List<Category> categories) {
        return categories.stream()
                .map(category -> new CategoryDto(
                        category.getName()
                ))
                .collect(Collectors.toList());
    }

    protected List<PostDto> getPostDtos(List<Post> posts) {
        return posts.stream()
                .map(post -> new PostDto(
                        post.getId(),
                        post.getTitle(),
                        post.getContent(),
                        post.getLikes(),
                        post.getDislikes(),
                        post.getCreatedDate(),
                        post.getUpdatedDate(),
                        getCategoryDtos(post.getCategory())
                        ))
                .collect(Collectors.toList());
    }

    protected List<CommentDto> getCommentDtos(List<Comment> comments) {
        return comments.stream()
                .map(comment -> new CommentDto(
                        comment.getId(),
                        comment.getContent(),
                        comment.getLikes(),
                        comment.getDislikes(),
                        comment.getCreatedDate(),
                        comment.getUpdatedDate()
                        ))
                .collect(Collectors.toList());
    }

    protected List<SubCommentDto> getSubCommentDtos(List<SubComment> subComments) {
        return subComments.stream()
                .map(subComment -> new SubCommentDto(
                        subComment.getId(),
                        subComment.getContent(),
                        subComment.getLikes(),
                        subComment.getDislikes(),
                        subComment.getCreatedDate(),
                        subComment.getUpdatedDate()
                        ))
                .collect(Collectors.toList());
    }
}
