package uni.plovdiv.online_library.service;

import org.springframework.stereotype.Service;

import uni.plovdiv.online_library.dto.UserStatusesDto;
import uni.plovdiv.online_library.jpa.UserRepository;
import uni.plovdiv.online_library.model.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

    public UserStatusesDto getUserStatuses() {
        List<User> users = userRepository.findAll();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        Date oneMonthAgo = calendar.getTime();

        long active = 0;
        long inactive = 0;
        for (User user: users) {
          if (user.getLastActiveAt().before(oneMonthAgo)) {
            inactive++;
          } else {
            active++;
          }
        }

        return new UserStatusesDto(active, inactive);
    }
}