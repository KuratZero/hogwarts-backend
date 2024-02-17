package ru.itmo.wp.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import ru.itmo.wp.domain.User;
import ru.itmo.wp.exception.SecurityHandlerException;
import ru.itmo.wp.security.annotations.JWTInterceptor;
import ru.itmo.wp.service.JwtService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
@RequiredArgsConstructor
public class JWTHandlerInterceptor implements HandlerInterceptor {
    private final JwtService jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (request.getAttribute("_user-interception") != null) {
            throw new SecurityHandlerException("Junk request");
        }

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        Method method = ((HandlerMethod) handler).getMethod();
        if (!method.isAnnotationPresent(JWTInterceptor.class)) {
            return true;
        }

        String header = request.getHeader("Authorization");
        if (header == null || header.length() < 8) {
            throw new SecurityHandlerException("Not correct jwt.");
        }
        User user = jwtService.find(header.substring(7));
        if(user == null) {
            throw new SecurityHandlerException("Not correct jwt (not found such user).");
        }
        request.setAttribute("_user-interception", user);
        return true;
    }
}
