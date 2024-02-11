package ru.itmo.wp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.itmo.wp.domain.Post;
import ru.itmo.wp.domain.User;
import ru.itmo.wp.domain.UserInfo;
import ru.itmo.wp.exception.ImageUploadException;
import ru.itmo.wp.exception.NoSuchResourceException;
import ru.itmo.wp.form.UserRegisterCredentials;
import ru.itmo.wp.repository.UserRepository;
import ru.itmo.wp.s3.S3Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final S3Service s3Service;

    @Value("${database.pass.salt}")
    private String salt;
    @Value("${database.pass.algo}")
    private String algo;


    private void updatePasswordSha(long id, String login, String password) {
        userRepository.updatePasswordSha(id, login, password, salt, algo);
    }

    public User findByLoginAndPassword(String login, String password) {
        return userRepository.findByLoginAndPassword(login, password, salt, algo)
                .orElseThrow(() -> new NoSuchResourceException("User not found: Invalid login or password"));
    }

    public boolean isLoginVacant(String login) {
        return !userRepository.findByLogin(login).isPresent();
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchResourceException("User not found"));
    }

    public User findByLogin(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new NoSuchResourceException("User not found."));
    }

    public void register(UserRegisterCredentials registerCredentials) {
        User user = new User();
        user.setName(registerCredentials.getName());
        user.setLogin(registerCredentials.getLogin());
        user = userRepository.save(user);
        updatePasswordSha(user.getId(), user.getLogin(), registerCredentials.getPassword());
    }

    public List<Post> findPostsByUserLogin(String login) {
        return findByLogin(login).getPosts();
    }

    public void updateUser(User user, String name, UserInfo info) {
        user.setName(name);
        info.setUserInfo(user);
        info.setId(user.getId());
        user.setInfo(info);
        userRepository.save(user);
    }

    public String findUserAvatar(Long id) {
        return s3Service.getAvatarById(id);
    }

    public void updateUserAvatar(User user, MultipartFile imageFile) {
        if (imageFile.getSize() > 10 * 1024) {
            throw new ImageUploadException("Image's weight is too large!");
        }

        BufferedImage image;
        try {
            image = ImageIO.read(imageFile.getInputStream());
        } catch (IOException e) {
            throw new ImageUploadException("Image read failed.");
        }

        if (image.getHeight() != image.getWidth() || image.getWidth() != 120) {
            throw new ImageUploadException("Image's must be 120x120 pixels.");

        }

        try {
            s3Service.updateAvatarById(user.getId(), imageFile.getBytes());
        } catch (IOException e) {
            throw new ImageUploadException("Server error.");
        }
    }
}
