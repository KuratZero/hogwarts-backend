package ru.itmo.wp.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.itmo.wp.domain.User;
import ru.itmo.wp.domain.UserInfo;
import ru.itmo.wp.form.UserRegisterCredentials;
import ru.itmo.wp.form.validator.UserCredentialsRegisterValidator;
import ru.itmo.wp.s3.S3Service;
import ru.itmo.wp.security.annotations.JWTInterceptor;
import ru.itmo.wp.service.JwtService;
import ru.itmo.wp.service.UserService;

import javax.imageio.ImageIO;
import javax.validation.Valid;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/1")
public class UserController {
    private final UserService userService;
    private final UserCredentialsRegisterValidator userCredentialsRegisterValidator;
    private final S3Service s3Service;

    @InitBinder("userRegisterCredentials")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(userCredentialsRegisterValidator);
    }

    @GetMapping("users/auth")
    @JWTInterceptor
    public User findUserByJwt(@RequestAttribute("_user-interception") User user) {
        return user;
    }

    @GetMapping("users")
    public List<User> findAllUsers() {
        return userService.findAll();
    }

    @GetMapping("user")
    public User findUserByLogin(@RequestParam("login") String login) {
        return userService.findByLogin(login).orElse(null);
    }

    @PutMapping("user")
    @JWTInterceptor
    public void updateUsersInfo(@RequestAttribute("_user-interception") User user,
                                @RequestParam("name") String name,
                                @Valid @RequestBody UserInfo info) {
        userService.updateUser(user, name, info);
    }

    @GetMapping("user/avatar")
    @SneakyThrows
    public String findUserAvatar(@RequestParam("id") Long id) {
        return s3Service.getAvatarById(id);
    }

    @PutMapping("user/avatar")
    @JWTInterceptor
    @SneakyThrows
    public void updateUserAvatar(@RequestAttribute("_user-interception") User user,
                                 @RequestParam("file") MultipartFile image) {
        BufferedImage imageFile = ImageIO.read(image.getInputStream());
        if (imageFile.getHeight() != imageFile.getWidth()
                || imageFile.getWidth() != 120) {
            return;
        }
        System.out.println(imageFile.getHeight());
        System.out.println(imageFile.getWidth());
        try {
            s3Service.updateAvatarById(user.getId(), image.getBytes());
        } catch (IOException ignored) {
        }
    }

    @PostMapping("users")
    public void registerUser(@Valid @RequestBody UserRegisterCredentials userRegisterCredentials) {
        userService.register(userRegisterCredentials);
    }
}
