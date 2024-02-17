package ru.itmo.wp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.itmo.wp.domain.Comment;
import ru.itmo.wp.domain.Post;
import ru.itmo.wp.domain.User;
import ru.itmo.wp.security.annotations.JWTInterceptor;
import ru.itmo.wp.service.PostService;
import ru.itmo.wp.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/1")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final UserService userService;

    @GetMapping("posts")
    public List<Post> findPosts() {
        return postService.findAll();
    }

    @GetMapping("posts/{login}")
    public List<Post> findPostsUser(@PathVariable("login") String login) {
        return userService.findPostsByUserLogin(login);
    }

    @PostMapping("post")
    @JWTInterceptor
    public void writePost(@RequestAttribute("_user-interception") User user,
                          @Valid @RequestBody Post post) {
        postService.writePost(post, user);
    }

    @GetMapping("post/{id}")
    public Post getPost(@PathVariable Long id) {
        return postService.find(id);
    }

    @DeleteMapping("post/{id}")
    @JWTInterceptor
    public void deletePost(@RequestAttribute("_user-interception") User user,
                           @PathVariable Long id) {
        postService.delete(user, id);
    }

    @PostMapping("/post/{id}")
    public void writeComment(@PathVariable long id,
                             @Valid @RequestBody Comment comment) {
//        if(bindingResult.hasErrors()) {
//            throw new ValidationException(bindingResult);
//        }
//        User user = jwtService.find(jwt);
//        if(user == null) {
//            throw new ValidationEntityException("User", "not found");
//        }
//        Post post = postService.find(id);
//        if(post == null) {
//            throw new ValidationEntityException("Post", "not found");
//        }
//        postService.writeComment(post, user, comment);
    }
}
