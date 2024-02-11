package ru.itmo.wp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.wp.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Transactional
    @Modifying
    @Query(value = "UPDATE users SET password_sha=digest(concat(?4, ?2, ?3), ?5) WHERE id=?1", nativeQuery = true)
    void updatePasswordSha(long id, String login, String password, String salt, String algorithm);

    @Query(value = "SELECT * FROM users WHERE login=?1 AND password_sha=cast(digest(concat(?3, ?1, ?2), ?4) AS text)", nativeQuery = true)
    Optional<User> findByLoginAndPassword(String login, String password, String salt, String algorithm);

    Optional<User> findByLogin(String login);
}
