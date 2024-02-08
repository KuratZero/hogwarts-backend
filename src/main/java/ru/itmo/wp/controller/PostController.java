package ru.itmo.wp.controller;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.itmo.wp.domain.Comment;
import ru.itmo.wp.domain.Post;
import ru.itmo.wp.domain.User;
import ru.itmo.wp.exception.ValidationException;
import ru.itmo.wp.exception.ValidationEntityException;
import ru.itmo.wp.service.JwtService;
import ru.itmo.wp.service.PostService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/1")
public class PostController {
    private final PostService postService;
    private final JwtService jwtService;

    public PostController(PostService postService, JwtController jwtController, JwtService jwtService) {
        this.postService = postService;
        this.jwtService = jwtService;
    }

    @GetMapping("posts")
    public List<Post> findPosts() {
        return postService.findAll();
    }

    @PostMapping("posts")
    public void writePost(@RequestParam String jwt, @Valid @RequestBody Post post, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        User user = jwtService.find(jwt);
        if (user == null) {
            throw new ValidationEntityException("User", "not found");
        }

        postService.writePost(post, user);
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

    @GetMapping("post/{id}")
    public Post getPost(@PathVariable Long id) {
        return postService.find(id);
    }
}
