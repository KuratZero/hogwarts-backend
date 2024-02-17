package ru.itmo.wp.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.wp.service.employee.UserHandlerService;

@RestController
@RequestMapping("/api/employee/1")
@RequiredArgsConstructor
public class UserHandler {
    private final UserHandlerService userHandlerService;

    @PostMapping("user/id")
    public Long findUserLoginByJwt(@RequestBody String jwt) {
        return userHandlerService.findUserIdByJwt(jwt);
    }
}
