package ru.itmo.wp.service;

import org.springframework.stereotype.Service;
import ru.itmo.wp.domain.User;
import ru.itmo.wp.domain.UserInfo;
import ru.itmo.wp.form.UserRegisterCredentials;
import ru.itmo.wp.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByLoginAndPassword(String login, String password) {
        return login == null || password == null ? null : userRepository.findByLoginAndPassword(login, password);
    }

    public User findById(Long id) {
        return id == null ? null : userRepository.findById(id).orElse(null);
    }

    public List<User> findAll() {
        return userRepository.findAllByOrderByIdDesc();
    }

    public boolean isLoginVacant(String login) {
        return !userRepository.findByLogin(login).isPresent();
    }

    public User register(UserRegisterCredentials registerCredentials) {
        User user = new User();
        user.setName(registerCredentials.getName());
        user.setLogin(registerCredentials.getLogin());
        user = userRepository.save(user);
        userRepository.updatePasswordSha(user.getId(), user.getLogin(), registerCredentials.getPassword());
        return user;
    }

    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public void updateUser(User user, String name, UserInfo info) {
        user.setName(name);
        info.setUserInfo(user);
        info.setId(user.getId());
        user.setInfo(info);
        userRepository.save(user);
    }
}
