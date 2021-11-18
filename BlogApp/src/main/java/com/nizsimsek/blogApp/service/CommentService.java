package com.nizsimsek.blogApp.service;

import com.nizsimsek.blogApp.dto.CommentDto;
import com.nizsimsek.blogApp.dto.converter.CommentDtoConverter;
import com.nizsimsek.blogApp.dto.request.CreateCommentReq;
import com.nizsimsek.blogApp.dto.request.UpdateCommentReq;
import com.nizsimsek.blogApp.exception.GeneralNotFoundException;
import com.nizsimsek.blogApp.model.Comment;
import com.nizsimsek.blogApp.model.Post;
import com.nizsimsek.blogApp.model.User;
import com.nizsimsek.blogApp.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentDtoConverter commentDtoConverter;
    private final UserService userService;
    private final PostService postService;

    public CommentService(CommentRepository commentRepository,
                          CommentDtoConverter commentDtoConverter,
                          UserService userService,
                          PostService postService) {
        this.commentRepository = commentRepository;
        this.commentDtoConverter = commentDtoConverter;
        this.userService = userService;
        this.postService = postService;
    }
    
    public CommentDto createComment(CreateCommentReq createCommentReq) {

    User user = userService.findUserById(createCommentReq.getAuthorId());
    Post post = postService.findPostById(createCommentReq.getPostId());

    Comment comment = new Comment(
            createCommentReq.getContent(),
            user,
            post
    );

    return commentDtoConverter.convert(commentRepository.save(comment));
}

    public List<CommentDto> getAllComments() {

        return commentDtoConverter.convertToCommentDtos(findAllComments());
    }

    public CommentDto getCommentById(String id) {

        return commentDtoConverter.convert(findCommentById(id));
    }

    public CommentDto updateComment(String id, UpdateCommentReq updateCommentReq) {

        Comment comment = findCommentById(id);

        Comment updatedComment = new Comment(
                comment.getId(),
                updateCommentReq.getContent(),
                comment.getLikes(),
                comment.getDislikes(),
                comment.getCreatedDate(),
                LocalDateTime.now(),
                comment.getAuthor(),
                comment.getPost(),
                Objects.requireNonNull(comment.getSubComments())
        );

        return commentDtoConverter.convert(commentRepository.save(updatedComment));
    }

    public void deleteCommentById(String id) {

        commentRepository.deleteById(id);
    }

    // BUSINESS LOGIC FOR COMMENT LIKE AND DISLIKE
    public CommentDto likeCommentById(String id) {

        Comment comment = findCommentById(id);
        long likeCount = comment.getLikes();
        if (likeCount >= 0) {
            likeCount += 1;
        }

        Comment updatedComment = new Comment(
                comment.getId(),
                comment.getContent(),
                likeCount,
                comment.getDislikes(),
                comment.getCreatedDate(),
                comment.getUpdatedDate(),
                comment.getAuthor(),
                comment.getPost(),
                comment.getSubComments()
        );

        return commentDtoConverter.convert(commentRepository.save(updatedComment));
    }

    public CommentDto unlikeCommentById(String id) {

        Comment comment = findCommentById(id);
        long likeCount = comment.getLikes();
        if (likeCount > 0) {
            likeCount -= 1;
        }

        Comment updatedComment = new Comment(
                comment.getId(),
                comment.getContent(),
                likeCount,
                comment.getDislikes(),
                comment.getCreatedDate(),
                comment.getUpdatedDate(),
                comment.getAuthor(),
                comment.getPost(),
                comment.getSubComments()
        );

        return commentDtoConverter.convert(commentRepository.save(updatedComment));
    }

    public CommentDto dislikeCommentById(String id) {

        Comment comment = findCommentById(id);
        long dislikeCount = comment.getLikes();
        if (dislikeCount >= 0) {
            dislikeCount += 1;
        }

        Comment updatedComment = new Comment(
                comment.getId(),
                comment.getContent(),
                comment.getLikes(),
                dislikeCount,
                comment.getCreatedDate(),
                comment.getUpdatedDate(),
                comment.getAuthor(),
                comment.getPost(),
                comment.getSubComments()
        );

        return commentDtoConverter.convert(commentRepository.save(updatedComment));
    }

    public CommentDto undislikeCommentById(String id) {

        Comment comment = findCommentById(id);
        long dislikeCount = comment.getLikes();
        if (dislikeCount > 0) {
            dislikeCount -= 1;
        }

        Comment updatedComment = new Comment(
                comment.getId(),
                comment.getContent(),
                comment.getLikes(),
                dislikeCount,
                comment.getCreatedDate(),
                comment.getUpdatedDate(),
                comment.getAuthor(),
                comment.getPost(),
                comment.getSubComments()
        );

        return commentDtoConverter.convert(commentRepository.save(updatedComment));
    }

    protected List<Comment> findAllComments() {

        return commentRepository.findAll();
    }

    protected Comment findCommentById(String id) {

        return commentRepository.findById(id)
                .orElseThrow(() -> new GeneralNotFoundException("Comment could not find by id : " + id));
    }
}
