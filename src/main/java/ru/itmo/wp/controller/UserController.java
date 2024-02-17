package ru.itmo.wp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.itmo.wp.domain.User;
import ru.itmo.wp.domain.UserInfo;
import ru.itmo.wp.form.UserRegisterCredentials;
import ru.itmo.wp.form.validator.UserCredentialsRegisterValidator;
import ru.itmo.wp.security.annotations.JWTInterceptor;
import ru.itmo.wp.service.UserService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/1")
public class UserController {
    private final UserService userService;
    private final UserCredentialsRegisterValidator userCredentialsRegisterValidator;

    @InitBinder("userRegisterCredentials")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(userCredentialsRegisterValidator);
    }

//    Login and register

    @GetMapping("user/auth")
    @JWTInterceptor
    public User findUserByJwt(@RequestAttribute("_user-interception") User user) {
        return user;
    }

    @PostMapping("user/register")
    public void registerUser(@Valid @RequestBody UserRegisterCredentials userRegisterCredentials) {
        userService.register(userRegisterCredentials);
    }

//    Utils

    @GetMapping("user")
    public User findUserByLogin(@RequestParam("login") String login) {
        return userService.findByLogin(login);
    }

//  Update

    @PutMapping("user")
    @JWTInterceptor
    public void updateUserInfo(@RequestAttribute("_user-interception") User user,
                                @RequestParam("name") String name,
                                @Valid @RequestBody UserInfo info) {
        userService.updateUser(user, name, info);
    }

}
