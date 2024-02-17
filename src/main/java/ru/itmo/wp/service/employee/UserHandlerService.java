package ru.itmo.wp.service.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itmo.wp.domain.User;
import ru.itmo.wp.exception.SecurityHandlerException;
import ru.itmo.wp.service.JwtService;

@Service
@RequiredArgsConstructor
public class UserHandlerService {
    private final JwtService jwtService;

    public Long findUserIdByJwt(String jwt) {
        if (jwt == null || jwt.length() < 8) {
            throw new SecurityHandlerException("Not correct jwt.");
        }
        User user = jwtService.find(jwt.substring(7));
        if (user == null) {
            throw new SecurityHandlerException("Not correct jwt (not found such user).");
        }
        return user.getId();
    }

}
