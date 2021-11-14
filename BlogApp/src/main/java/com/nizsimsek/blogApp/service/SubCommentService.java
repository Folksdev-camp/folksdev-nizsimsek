package com.nizsimsek.blogApp.service;

import com.nizsimsek.blogApp.dto.SubCommentDto;
import com.nizsimsek.blogApp.dto.converter.SubCommentDtoConverter;
import com.nizsimsek.blogApp.dto.request.*;
import com.nizsimsek.blogApp.exception.GeneralNotFoundException;
import com.nizsimsek.blogApp.model.*;
import com.nizsimsek.blogApp.repository.SubCommentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubCommentService {
    
    private final SubCommentRepository subCommentRepository;
    private final SubCommentDtoConverter subCommentDtoConverter;
    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;

    public SubCommentService(SubCommentRepository subCommentRepository,
                             SubCommentDtoConverter subCommentDtoConverter,
                             UserService userService,
                             PostService postService, CommentService commentService) {
        this.subCommentRepository = subCommentRepository;
        this.subCommentDtoConverter = subCommentDtoConverter;
        this.userService = userService;
        this.postService = postService;
        this.commentService = commentService;
    }

    public SubCommentDto createSubComment(CreateSubCommentReq createSubCommentReq) {

        User user = userService.findUserById(createSubCommentReq.getAuthorId());
        Post post = postService.findPostById(createSubCommentReq.getPostId());
        Comment comment = commentService.findCommentById(createSubCommentReq.getCommentId());

        SubComment subComment = new SubComment(
                createSubCommentReq.getContent(),
                createSubCommentReq.getLike(),
                user,
                post,
                comment
        );

        return subCommentDtoConverter.convert(subCommentRepository.save(subComment));
    }

    public List<SubCommentDto> getAllSubComments() {

        return subCommentRepository.findAll()
                .stream().map(subCommentDtoConverter::convert)
                .collect(Collectors.toList());
    }

    public SubCommentDto getSubCommentById(String id) {

        return subCommentDtoConverter.convert(findSubCommentById(id));
    }

    public SubCommentDto updateSubComment(String id, UpdateSubCommentReq updateSubCommentReq) {

        SubComment subComment = findSubCommentById(id);

        SubComment updatedSubComment = new SubComment(
                subComment.getId(),
                updateSubCommentReq.getContent(),
                subComment.getLikes(),
                subComment.getCreatedDate(),
                LocalDateTime.now(),
                subComment.getAuthor(),
                subComment.getPost(),
                subComment.getComment()
        );

        return subCommentDtoConverter.convert(subCommentRepository.save(updatedSubComment));
    }

    public void deleteSubCommentById(String id) {
        subCommentRepository.deleteById(id);
    }

    protected SubComment findSubCommentById(String id) {
        return subCommentRepository.findById(id)
                .orElseThrow(() -> new GeneralNotFoundException("SubComment could not find by id : " + id));
    }
}
