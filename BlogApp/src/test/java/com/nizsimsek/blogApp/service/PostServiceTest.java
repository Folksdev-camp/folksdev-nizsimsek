package com.nizsimsek.blogApp.service;

import com.nizsimsek.blogApp.TestSupport;
import com.nizsimsek.blogApp.dto.PostDto;
import com.nizsimsek.blogApp.dto.converter.PostDtoConverter;
import com.nizsimsek.blogApp.dto.request.CreatePostReq;
import com.nizsimsek.blogApp.dto.request.UpdatePostReq;
import com.nizsimsek.blogApp.exception.GeneralNotFoundException;
import com.nizsimsek.blogApp.model.Category;
import com.nizsimsek.blogApp.model.Post;
import com.nizsimsek.blogApp.model.User;
import com.nizsimsek.blogApp.repository.PostRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class PostServiceTest extends TestSupport {

    private PostRepository postRepository;
    private PostDtoConverter postDtoConverter;
    private UserService userService;
    private CategoryService categoryService;

    private PostService postService;

    @BeforeEach
    void setUp() {

        postRepository = mock(PostRepository.class);
        postDtoConverter = mock(PostDtoConverter.class);
        userService = mock(UserService.class);
        categoryService = mock(CategoryService.class);

        postService = new PostService(postRepository, postDtoConverter, userService, categoryService);
    }

    @Test
    void testCreatePost_whenCreatePostReqValid_shouldReturnPostDto() {

        CreatePostReq createPostReq = generateCreatePostReq();
        Post post = generatePost();
        PostDto postDto = generatePostDto();
        User user = generateUser();
        List<Category> categoryList = generateCategories();

        when(categoryService.getCategories(List.of("id"))).thenReturn(categoryList);
        when(userService.findUserById("id")).thenReturn(user);
        when(postRepository.save(post)).thenReturn(post);
        when(postDtoConverter.convert(post)).thenReturn(postDto);

        PostDto result = postService.createPost(createPostReq);

        assertEquals(postDto, result);

        verify(categoryService).getCategories(List.of("id"));
        verify(userService).findUserById("id");
        verify(postRepository).save(post);
        verify(postDtoConverter).convert(post);
    }

    @Test
    void testGetAllPosts_shouldReturnPostDtos() {

        List<Post> posts = generatePosts();
        List<PostDto> postDtos = generatePostDtos();

        when(postRepository.findAll()).thenReturn(posts);
        when(postDtoConverter.convertToPostDtos(posts)).thenReturn(postDtos);

        List<PostDto> result = postService.getAllPosts();

        assertEquals(postDtos, result);

        verify(postRepository).findAll();
        verify(postDtoConverter).convertToPostDtos(posts);
    }

    @Test
    void testGetPostById_whenIdNotExist_shouldThrowGeneralNotFoundException() {

        when(postRepository.findById("id")).thenThrow(GeneralNotFoundException.class);

        assertThrows(GeneralNotFoundException.class, () -> postService.getPostById("id"));

        verify(postRepository).findById("id");
        verifyNoInteractions(postDtoConverter);
    }

    @Test
    void testGetPostById_whenIdExist_shouldReturnPostDto() {

        Post post = generatePost();
        PostDto postDto = generatePostDto();

        when(postRepository.findById("id")).thenReturn(java.util.Optional.ofNullable(post));
        assert post != null;
        when(postDtoConverter.convert(post)).thenReturn(postDto);

        PostDto result = postService.getPostById("id");

        assertEquals(postDto, result);

        verify(postRepository).findById("id");
        verify(postDtoConverter).convert(post);
    }

    @Test
    void testUpdatePost_whenUpdatePostReqInValid_shouldThrowGeneralNotFoundException() {

        UpdatePostReq updatePostReq = generateUpdatePostReq();

        when(postRepository.findById("id")).thenThrow(GeneralNotFoundException.class);

        assertThrows(GeneralNotFoundException.class, () -> postService.updatePost("id", updatePostReq));

        verify(postRepository).findById("id");
        verifyNoInteractions(postDtoConverter);
    }

    @Test
    void testUpdatePost_whenUpdatePostReqValid_shouldReturnPostDto() {
        UpdatePostReq updatePostReq = generateUpdatePostReq();
        Post post = generatePost();
        PostDto postDto = generatePostDto();

        when(postRepository.findById("id")).thenReturn(java.util.Optional.ofNullable(post));
        assert post != null;
        when(postRepository.save(post)).thenReturn(post);
        when(postDtoConverter.convert(post)).thenReturn(postDto);

        PostDto result = postService.updatePost("id", updatePostReq);

        assertEquals(postDto, result);

        verify(postRepository).findById("id");
        verify(postRepository).save(post);
        verify(postDtoConverter).convert(post);
    }

    @Test
    void testDeletePostById_whenIdNotExist_shouldThrowGeneralNotFoundException() {

        when(postRepository.findById("id")).thenThrow(GeneralNotFoundException.class);

        assertThrows(GeneralNotFoundException.class, () -> postService.getPostById("id"));

        verify(postRepository).findById("id");
        verifyNoInteractions(postDtoConverter);
    }

    @Test
    void testDeletePostById_whenIdExist() {

        postService.deletePostById("id");

        verify(postRepository).deleteById("id");
    }

    @Test
    void testLikePostById_whenIdNotExist_shouldThrowGeneralNotFoundException() {

        when(postRepository.findById("id")).thenThrow(GeneralNotFoundException.class);

        assertThrows(GeneralNotFoundException.class, () -> postService.findPostById("id"));

        verify(postRepository).findById("id");
        verifyNoInteractions(postDtoConverter);
    }

    @Test
    void testLikePostById_whenIdExist_shouldReturnPostDto() {

        Post post = generatePostAllFields();
        PostDto postDto = generatePostDto();
        long likeCount = post.getLikes();

        assert likeCount >= 0;
        likeCount += 1;

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

        when(postRepository.findById("id")).thenReturn(java.util.Optional.of(updatedPost));
        when(postRepository.save(updatedPost)).thenReturn(updatedPost);
        when(postDtoConverter.convert(updatedPost)).thenReturn(postDto);

        PostDto result = postService.likePostById("id");

        assertEquals(postDto, result);

        verify(postRepository).findById("id");
        verify(postRepository).save(updatedPost);
        verify(postDtoConverter).convert(post);
    }

    @Test
    void testUnlikePostById_whenIdNotExist_shouldThrowGeneralNotFoundException() {

        when(postRepository.findById("id")).thenThrow(GeneralNotFoundException.class);

        assertThrows(GeneralNotFoundException.class, () -> postService.findPostById("id"));

        verify(postRepository).findById("id");
        verifyNoInteractions(postDtoConverter);
    }

    @Test
    void testUnlikePostById_whenIdExist_shouldReturnPostDto() {

        Post post = generatePostAllFields();
        PostDto postDto = generatePostDto();
        long likeCount = post.getLikes();

        assert likeCount > 0;
        likeCount -= 1;

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

        when(postRepository.findById("id")).thenReturn(java.util.Optional.of(updatedPost));
        when(postRepository.save(updatedPost)).thenReturn(updatedPost);
        when(postDtoConverter.convert(updatedPost)).thenReturn(postDto);

        PostDto result = postService.unlikePostById("id");

        assertEquals(postDto, result);

        verify(postRepository).findById("id");
        verify(postRepository).save(updatedPost);
        verify(postDtoConverter).convert(post);
    }

    @Test
    void testDislikePostById_whenIdNotExist_shouldThrowGeneralNotFoundException() {

        when(postRepository.findById("id")).thenThrow(GeneralNotFoundException.class);

        assertThrows(GeneralNotFoundException.class, () -> postService.findPostById("id"));

        verify(postRepository).findById("id");
        verifyNoInteractions(postDtoConverter);
    }

    @Test
    void testDislikePostById_whenIdExist_shouldReturnPostDto() {

        Post post = generatePostAllFields();
        PostDto postDto = generatePostDto();
        long dislikeCount = post.getDislikes();

        assert dislikeCount >= 0;
        dislikeCount += 1;

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

        when(postRepository.findById("id")).thenReturn(java.util.Optional.of(updatedPost));
        when(postRepository.save(updatedPost)).thenReturn(updatedPost);
        when(postDtoConverter.convert(updatedPost)).thenReturn(postDto);

        PostDto result = postService.dislikePostById("id");

        assertEquals(postDto, result);

        verify(postRepository).findById("id");
        verify(postRepository).save(updatedPost);
        verify(postDtoConverter).convert(post);
    }

    @Test
    void testUndislikePostById_whenIdNotExist_shouldThrowGeneralNotFoundException() {

        when(postRepository.findById("id")).thenThrow(GeneralNotFoundException.class);

        assertThrows(GeneralNotFoundException.class, () -> postService.findPostById("id"));

        verify(postRepository).findById("id");
        verifyNoInteractions(postDtoConverter);
    }

    @Test
    void testUndislikePostById_whenIdExist_shouldReturnPostDto() {

        Post post = generatePostAllFields();
        PostDto postDto = generatePostDto();
        long dislikeCount = post.getDislikes();

        assert dislikeCount > 0;
        dislikeCount -= 1;

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

        when(postRepository.findById("id")).thenReturn(java.util.Optional.of(updatedPost));
        when(postRepository.save(updatedPost)).thenReturn(updatedPost);
        when(postDtoConverter.convert(updatedPost)).thenReturn(postDto);

        PostDto result = postService.undislikePostById("id");

        assertEquals(postDto, result);

        verify(postRepository).findById("id");
        verify(postRepository).save(updatedPost);
        verify(postDtoConverter).convert(post);
    }

    @AfterEach
    void tearDown() {
    }
}