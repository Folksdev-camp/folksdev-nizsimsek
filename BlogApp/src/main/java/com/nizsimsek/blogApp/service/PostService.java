package com.nizsimsek.blogApp.service;

import com.nizsimsek.blogApp.dto.PostDto;
import com.nizsimsek.blogApp.dto.converter.PostDtoConverter;
import com.nizsimsek.blogApp.dto.request.*;
import com.nizsimsek.blogApp.exception.GeneralNotFoundException;
import com.nizsimsek.blogApp.model.*;
import com.nizsimsek.blogApp.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostDtoConverter postDtoConverter;
    private final UserService userService;
    private final CategoryService categoryService;

    public PostService(PostRepository postRepository,
                       PostDtoConverter postDtoConverter,
                       UserService userService, CategoryService categoryService) {
        this.postRepository = postRepository;
        this.postDtoConverter = postDtoConverter;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    public PostDto createPost(CreatePostReq createPostReq) {

        User user = userService.findUserById(createPostReq.getAuthorId());
        List<Category> categories = categoryService.getCategories(createPostReq.getCategoryIds());

        Post post = new Post(
                createPostReq.getTitle(),
                createPostReq.getContent(),
                categories,
                user
        );

        return postDtoConverter.convert(postRepository.save(post));
    }

    public List<PostDto> getAllPosts() {

        return postRepository.findAll()
                .stream()
                .map(postDtoConverter::convert)
                .collect(Collectors.toList());
    }

    public PostDto getPostById(String id) {

        return postDtoConverter.convert(findPostById(id));
    }

    public PostDto updatePost(String id, UpdatePostReq updatePostReq) {

        Post post = findPostById(id);
        List<Category> categories = categoryService.getCategories(updatePostReq.getCategoryIds());

        Post updatedPost = new Post(
                post.getId(),
                updatePostReq.getTitle(),
                updatePostReq.getContent(),
                post.getLikes(),
                post.getDislikes(),
                post.getCreatedDate(),
                LocalDateTime.now(),
                categories,
                post.getAuthor(),
                post.getComments()
        );

        return postDtoConverter.convert(postRepository.save(updatedPost));
    }

    public void deletePostById(String id) {
        postRepository.deleteById(id);
    }

    // BUSINESS LOGIC FOR POST LIKE AND DISLIKE
    public PostDto likePostById(String id) {

        Post post = findPostById(id);
        long likeCount = post.getLikes();
        if (likeCount >= 0) {
            likeCount += 1;
        }

        Post updatedPost = new Post(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                likeCount,
                post.getDislikes(),
                post.getCreatedDate(),
                post.getUpdatedDate(),
                post.getCategory(),
                post.getAuthor(),
                post.getComments()
        );

        return postDtoConverter.convert(postRepository.save(updatedPost));
    }

    public PostDto unlikePostById(String id) {

        Post post = findPostById(id);
        long likeCount = post.getLikes();
        if (likeCount > 0) {
            likeCount -= 1;
        }

        Post updatedPost = new Post(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                likeCount,
                post.getDislikes(),
                post.getCreatedDate(),
                post.getUpdatedDate(),
                post.getCategory(),
                post.getAuthor(),
                post.getComments()
        );

        return postDtoConverter.convert(postRepository.save(updatedPost));
    }

    public PostDto dislikePostById(String id) {

        Post post = findPostById(id);
        long dislikeCount = post.getDislikes();
        if (dislikeCount >= 0) {
            dislikeCount += 1;
        }

        Post updatedPost = new Post(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getLikes(),
                dislikeCount,
                post.getCreatedDate(),
                post.getUpdatedDate(),
                post.getCategory(),
                post.getAuthor(),
                post.getComments()
        );

        return postDtoConverter.convert(postRepository.save(updatedPost));
    }

    public PostDto undislikePostById(String id) {

        Post post = findPostById(id);
        long dislikeCount = post.getDislikes();
        if (dislikeCount > 0) {
            dislikeCount -= 1;
        }

        Post updatedPost = new Post(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getLikes(),
                dislikeCount,
                post.getCreatedDate(),
                post.getUpdatedDate(),
                post.getCategory(),
                post.getAuthor(),
                post.getComments()
        );

        return postDtoConverter.convert(postRepository.save(updatedPost));
    }

    protected Post findPostById(String id) {

        return postRepository.findById(id)
                .orElseThrow(() -> new GeneralNotFoundException("Post could not find by id : " + id));
    }

}
