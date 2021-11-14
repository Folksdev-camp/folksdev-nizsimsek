package com.nizsimsek.blogApp.controller;

import com.nizsimsek.blogApp.dto.SubCommentDto;
import com.nizsimsek.blogApp.dto.request.*;
import com.nizsimsek.blogApp.service.SubCommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/subcomment")
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
    public ResponseEntity<SubCommentDto> updateSubComment(@PathVariable String id, @RequestBody UpdateSubCommentReq updateSubCommentReq) {
        return ResponseEntity.ok(subCommentService.updateSubComment(id, updateSubCommentReq));
    }

    @DeleteMapping(value = "/{id}")
    public void deleteSubComment(@PathVariable String id) {
        subCommentService.deleteSubCommentById(id);
    }
}
