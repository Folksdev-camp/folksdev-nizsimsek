package com.nizsimsek.blogApp.controller;

import com.nizsimsek.blogApp.dto.PostDto;
import com.nizsimsek.blogApp.dto.request.CreatePostReq;
import com.nizsimsek.blogApp.dto.request.UpdatePostReq;
import com.nizsimsek.blogApp.service.PostService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<PostDto>> getPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PostDto> getPost(@PathVariable String id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @PostMapping
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody CreatePostReq createPostReq) {
        return ResponseEntity.ok(postService.createPost(createPostReq));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<PostDto> updatePost(@PathVariable String id, @Valid @RequestBody UpdatePostReq updatePostReq) {
        return ResponseEntity.ok(postService.updatePost(id, updatePostReq));
    }

    @PatchMapping(value = "/like/{id}")
    public ResponseEntity<PostDto> likePostById(@PathVariable String id) {
        return ResponseEntity.ok(postService.likePostById(id));
    }

    @PatchMapping(value = "/unlike/{id}")
    public ResponseEntity<PostDto> unlikePostById(@PathVariable String id) {
        return ResponseEntity.ok(postService.unlikePostById(id));
    }

    @PatchMapping(value = "/dislike/{id}")
    public ResponseEntity<PostDto> dislikePostById(@PathVariable String id) {
        return ResponseEntity.ok(postService.dislikePostById(id));
    }

    @PatchMapping(value = "/undislike/{id}")
    public ResponseEntity<PostDto> undislikePostById(@PathVariable String id) {
        return ResponseEntity.ok(postService.undislikePostById(id));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deletePostById(@PathVariable String id) {
        postService.deletePostById(id);
        return ResponseEntity.ok("Post deleted..");
    }
}
