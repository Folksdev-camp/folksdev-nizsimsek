package com.nizsimsek.blogApp.controller;

import com.nizsimsek.blogApp.dto.SubCommentDto;
import com.nizsimsek.blogApp.dto.request.CreateSubCommentReq;
import com.nizsimsek.blogApp.dto.request.UpdateSubCommentReq;
import com.nizsimsek.blogApp.service.SubCommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/subcomments")
public class SubCommentController {

    private final SubCommentService subCommentService;

    public SubCommentController(SubCommentService subCommentService) {
        this.subCommentService = subCommentService;
    }

    @GetMapping
    public ResponseEntity<List<SubCommentDto>> getSubComments() {
        return ResponseEntity.ok(subCommentService.getAllSubComments());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<SubCommentDto> getSubComment(@PathVariable String id) {
        return ResponseEntity.ok(subCommentService.getSubCommentById(id));
    }

    @PostMapping
    public ResponseEntity<SubCommentDto> createSubComment(@Valid @RequestBody CreateSubCommentReq createSubCommentReq) {
        return ResponseEntity.ok(subCommentService.createSubComment(createSubCommentReq));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<SubCommentDto> updateSubComment(@PathVariable String id, @Valid @RequestBody UpdateSubCommentReq updateSubCommentReq) {
        return ResponseEntity.ok(subCommentService.updateSubComment(id, updateSubCommentReq));
    }

    @PatchMapping(value = "/like/{id}")
    public ResponseEntity<SubCommentDto> likeSubComment(@PathVariable String id) {
        return ResponseEntity.ok(subCommentService.likeSubCommentById(id));
    }

    @PatchMapping(value = "/unlike/{id}")
    public ResponseEntity<SubCommentDto> unlikeSubComment(@PathVariable String id) {
        return ResponseEntity.ok(subCommentService.unlikeSubCommentById(id));
    }

    @PatchMapping(value = "/dislike/{id}")
    public ResponseEntity<SubCommentDto> dislikeSubComment(@PathVariable String id) {
        return ResponseEntity.ok(subCommentService.dislikeSubCommentById(id));
    }

    @PatchMapping(value = "/undislike/{id}")
    public ResponseEntity<SubCommentDto> undislikeSubComment(@PathVariable String id) {
        return ResponseEntity.ok(subCommentService.undislikeSubCommentById(id));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteSubCommentById(@PathVariable String id) {
        subCommentService.deleteSubCommentById(id);
        return ResponseEntity.ok("SubComment deleted..");
    }
}
