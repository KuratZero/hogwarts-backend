package ru.itmo.wp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.wp.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Transactional
    @Modifying
    @Query(value = "UPDATE users SET password_sha=digest(concat('8960cfckfb313ass', ?2, ?3), 'sha256') WHERE id=?1", nativeQuery = true)
    void updatePasswordSha(long id, String login, String password);

    @Query(value = "SELECT * FROM users WHERE login=?1 AND password_sha=cast(digest(concat('8960cfckfb313ass', ?1, ?2), 'sha256') AS text)", nativeQuery = true)
    User findByLoginAndPassword(String login, String password);

    List<User> findAllByOrderByIdDesc();

//    User findByLogin(String login);

    Optional<User> findByLogin(String login);
}
