package sr57.ftn.reddit.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sr57.ftn.reddit.project.model.entity.User;
import sr57.ftn.reddit.project.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    final UserRepository userRepository;

    final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findOne(Integer user_id) {
        return userRepository.findById(user_id).orElseGet(null);
    }

    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    public void remove(Integer id) {
        this.userRepository.deleteById(id);
    }

    public User save(User user) {
        return this.userRepository.save(user);
    }

    public User findByUsername(String username) {
        Optional<User> user = userRepository.findFirstByUsername(username);
        if (user.isPresent()) {
            return user.get();
        }
        return null;
    }

    public Optional<User> findFirstByUsername(String username) {
        return userRepository.findFirstByUsername(username);
    }

    public Optional<User> findFirstByEmail(String email) {
        return userRepository.findFirstByEmail(email);
    }

    public User findOneWithPosts(Integer user_id) {
        return userRepository.findOneWithPosts(user_id);
    }
}
