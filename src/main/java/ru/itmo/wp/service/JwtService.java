package ru.itmo.wp.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.itmo.wp.domain.User;

@Service
public class JwtService {
    private final Algorithm algorithm;
    private final JWTVerifier verifier;
    private final UserService userService;

    public JwtService(UserService userService, @Value("@jwt.secret@") String secret) {
        this.userService = userService;
        this.algorithm = Algorithm.HMAC256(secret);
        this.verifier = JWT.require(algorithm).build();
    }

    public String create(User user) {
        try {
            return JWT.create()
                    .withClaim("userId", user.getId())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Can't create JWT.");
        }
    }

    public User find(String jwt) {
        try {
            DecodedJWT decodedJwt = verifier.verify(jwt);
            return userService.findById(decodedJwt.getClaim("userId").asLong());
        } catch (JWTVerificationException exception) {
            return null;
        }
    }
}
