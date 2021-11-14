package com.nizsimsek.blogApp.controller;

import com.nizsimsek.blogApp.dto.CommentDto;
import com.nizsimsek.blogApp.dto.request.*;
import com.nizsimsek.blogApp.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<List<CommentDto>> getComments() {
        return ResponseEntity.ok(commentService.getAllComments());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CommentDto> getComment(@PathVariable String id) {
        return ResponseEntity.ok(commentService.getCommentById(id));
    }

    @PostMapping
    public ResponseEntity<CommentDto> createComment(@Valid @RequestBody CreateCommentReq createCommentReq) {
        return ResponseEntity.ok(commentService.createComment(createCommentReq));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable String id, @RequestBody UpdateCommentReq updateCommentReq) {
        return ResponseEntity.ok(commentService.updateComment(id, updateCommentReq));
    }

    @DeleteMapping(value = "/{id}")
    public void deleteComment(@PathVariable String id) {
        commentService.deleteCommentById(id);
    }
}
