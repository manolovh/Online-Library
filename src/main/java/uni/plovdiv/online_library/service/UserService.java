package uni.plovdiv.online_library.service;

import org.springframework.stereotype.Service;

import uni.plovdiv.online_library.jpa.UserRepository;
import uni.plovdiv.online_library.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> allUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }   
}