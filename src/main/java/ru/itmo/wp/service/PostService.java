package ru.itmo.wp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itmo.wp.domain.Comment;
import ru.itmo.wp.domain.Post;
import ru.itmo.wp.domain.User;
import ru.itmo.wp.exception.NoSuchResourceException;
import ru.itmo.wp.repository.PostRepository;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public List<Post> findAll() {
        return postRepository.findAllByOrderByCreationTimeDesc();
    }

    public void writePost(Post post, User user) {
        post.setText(post.getText().trim());
        post.setUser(user);
        postRepository.save(post);
    }

    public void writeComment(Long id, User user, Comment comment) {
        Post post = find(id);

        comment.setPost(post);
        comment.setUser(user);
        post.getComments().add(comment);
        postRepository.save(post);
    }

    public Post find(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new NoSuchResourceException("No such post"));
    }

    public void delete(User user, Long id) {
        Post post = find(id);
        if (!Objects.equals(post.getUser().getId(), user.getId())) {
            throw new SecurityException();
        }
        postRepository.delete(post);
    }
}
