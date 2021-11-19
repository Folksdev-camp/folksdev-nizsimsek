package com.nizsimsek.blogApp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nizsimsek.blogApp.dto.converter.*;
import com.nizsimsek.blogApp.model.*;
import com.nizsimsek.blogApp.repository.*;
import com.nizsimsek.blogApp.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application.properties")
@DirtiesContext
@AutoConfigureMockMvc
public class IntegrationTestSupport extends TestSupport {

    @Autowired
    public UserService userService;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public UserDtoConverter userDtoConverter;

    @Autowired
    public CategoryService categoryService;

    @Autowired
    public CategoryRepository categoryRepository;

    @Autowired
    public CategoryDtoConverter categoryDtoConverter;

    @Autowired
    public PostService postService;

    @Autowired
    public PostRepository postRepository;

    @Autowired
    public PostDtoConverter postDtoConverter;

    @Autowired
    public CommentService commentService;

    @Autowired
    public CommentRepository commentRepository;

    @Autowired
    public CommentDtoConverter commentDtoConverter;

    @Autowired
    public SubCommentService subCommentService;

    @Autowired
    public SubCommentRepository subCommentRepository;

    @Autowired
    public SubCommentDtoConverter subCommentDtoConverter;

    @Autowired
    public MockMvc mockMvc;

    public final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
    }

    public User generateUser(int i) {
        return new User("id", "username-" + i, "username" + i + "@email.com", "firstName", "lastName", "password");
    }

    public User generateUserDefaultFields() {
        return new User("id", "username", "email", "firstName", "lastName", "password");
    }

    public Category generateCategory(int i) {
        return new Category("id", "name-" + i);
    }

    public List<Category> generateCategoryList(int size) {
        return IntStream.range(0, size)
                .mapToObj(this::generateCategory)
                .collect(Collectors.toList());
    }

    public Post generatePost(int i) {
        return new Post("id", "title", "content", generateCategoryList(i), generateUserDefaultFields());
    }

    public Comment generateComment(int i) {
        return new Comment("id" + i, "content", 0, generateUser(i), generatePost(i));
    }

    public SubComment generateSubComment(int i) {
        return new SubComment("id" + i, "content", 0, generateUser(i), generateComment(i));
    }

    public List<SubComment> generateSubCommentList(int size) {
        return IntStream.range(0, size)
                .mapToObj(this::generateSubComment)
                .collect(Collectors.toList());
    }
}
