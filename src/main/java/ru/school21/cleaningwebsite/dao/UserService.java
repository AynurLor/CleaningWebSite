package ru.school21.cleaningwebsite.dao;

import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.school21.cleaningwebsite.models.User;

import java.util.List;

@Service
@Component
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User getUser(Integer id_telegram) {
        return (User)userRepository.findByIdTelegram(id_telegram);
    }

    public List<User> getAllUser() {
        return userRepository.findAll();
    }
}
