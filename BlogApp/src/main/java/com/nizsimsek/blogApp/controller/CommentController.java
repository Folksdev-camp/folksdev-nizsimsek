package com.nizsimsek.blogApp.controller;

import com.nizsimsek.blogApp.dto.CommentDto;
import com.nizsimsek.blogApp.dto.request.CreateCommentReq;
import com.nizsimsek.blogApp.dto.request.UpdateCommentReq;
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

    @PatchMapping(value = "/{id}/like")
    public ResponseEntity<CommentDto> likePost(@PathVariable String id) {
        return ResponseEntity.ok(commentService.likeCommentById(id));
    }

    @PatchMapping(value = "/{id}/unlike")
    public ResponseEntity<CommentDto> unlikePost(@PathVariable String id) {
        return ResponseEntity.ok(commentService.unlikeCommentById(id));
    }

    @PatchMapping(value = "/{id}/dislike")
    public ResponseEntity<CommentDto> dislikePost(@PathVariable String id) {
        return ResponseEntity.ok(commentService.dislikeCommentById(id));
    }

    @PatchMapping(value = "/{id}/undislike")
    public ResponseEntity<CommentDto> undislikePost(@PathVariable String id) {
        return ResponseEntity.ok(commentService.undislikeCommentById(id));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable String id) {
        commentService.deleteCommentById(id);
        return ResponseEntity.ok("Comment deleted..");
    }
}
