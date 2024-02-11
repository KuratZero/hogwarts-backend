package ru.itmo.wp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.itmo.wp.form.UserCredentials;
import ru.itmo.wp.service.JwtService;
import ru.itmo.wp.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/1")
public class JwtController {
    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping("jwt")
    public String create(@RequestBody UserCredentials userCredentials) {
        return jwtService.create(
                userService.findByLoginAndPassword(
                        userCredentials.getLogin(), userCredentials.getPassword()));
    }
}
