package com.nizsimsek.blogApp.service;

import com.nizsimsek.blogApp.dto.CommentDto;
import com.nizsimsek.blogApp.dto.converter.CommentDtoConverter;
import com.nizsimsek.blogApp.dto.request.*;
import com.nizsimsek.blogApp.exception.GeneralNotFoundException;
import com.nizsimsek.blogApp.model.*;
import com.nizsimsek.blogApp.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
            createCommentReq.getLike(),
            user,
            post
    );

    return commentDtoConverter.convert(commentRepository.save(comment));
}

    public List<CommentDto> getAllComments() {

        return commentRepository.findAll()
                .stream().map(commentDtoConverter::convert)
                .collect(Collectors.toList());
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
                comment.getCreatedDate(),
                LocalDateTime.now(),
                comment.getAuthor(),
                comment.getPost(),
                comment.getSubComments()
        );

        return commentDtoConverter.convert(commentRepository.save(updatedComment));
    }

    public void deleteCommentById(String id) {
        commentRepository.deleteById(id);
    }

    protected Comment findCommentById(String id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new GeneralNotFoundException("Comment could not find by id : " + id));
    }
}
