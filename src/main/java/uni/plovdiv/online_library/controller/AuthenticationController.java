package uni.plovdiv.online_library.controller;

import java.util.Date;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.AllArgsConstructor;
import uni.plovdiv.online_library.UserRole;
import uni.plovdiv.online_library.dto.RegisterUserDto;
import uni.plovdiv.online_library.jpa.UserRepository;
import uni.plovdiv.online_library.model.User;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class AuthenticationController {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute RegisterUserDto registerUserDto) {
        User user = new User(registerUserDto.getEmail(), registerUserDto.getPassword(), UserRole.USER, new Date());
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email is already taken.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "login";
    }

    @GetMapping("/index")
    public String showIndexPage() {
        return "index";
    }

    @GetMapping("/admin-panel")
    public String showAdminPanel() {
        return "adminPanel";
    }


    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }
}
