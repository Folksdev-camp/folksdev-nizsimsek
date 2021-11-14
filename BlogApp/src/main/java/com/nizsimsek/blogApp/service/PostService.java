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

    public PostService(PostRepository postRepository,
                       PostDtoConverter postDtoConverter,
                       UserService userService) {
        this.postRepository = postRepository;
        this.postDtoConverter = postDtoConverter;
        this.userService = userService;
    }

    public PostDto createPost(CreatePostReq createPostReq) {

        User user = userService.findUserById(createPostReq.getAuthorId());

        Post post = new Post(
                createPostReq.getTitle(),
                createPostReq.getContent(),
                createPostReq.getLike(),
                user
        );

        return postDtoConverter.convert(postRepository.save(post));
    }

    public List<PostDto> getAllPosts() {

        return postRepository.findAll()
                .stream().map(postDtoConverter::convert)
                .collect(Collectors.toList());
    }

    public PostDto getPostById(String id) {

        return postDtoConverter.convert(findPostById(id));
    }

    public PostDto updatePost(String id, UpdatePostReq updatePostReq) {

        Post post = findPostById(id);

        Post updatedPost = new Post(
                post.getId(),
                updatePostReq.getTitle(),
                updatePostReq.getContent(),
                post.getLikes(),
                post.getCreatedDate(),
                LocalDateTime.now(),
                post.getAuthor(),
                post.getComments(),
                post.getSubComments()
        );

        return postDtoConverter.convert(postRepository.save(updatedPost));
    }

    public void deletePostById(String id) {
        postRepository.deleteById(id);
    }

    protected Post findPostById(String id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new GeneralNotFoundException("Post could not find by id : " + id));
    }
}
