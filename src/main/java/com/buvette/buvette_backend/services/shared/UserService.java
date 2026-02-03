package com.buvette.buvette_backend.services.shared;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.buvette.buvette_backend.model.client.User;
import com.buvette.buvette_backend.repository.shared.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository repo;
    @Autowired
    private PasswordEncoder pass;

    public void engister(User u) {
        u.setPassword(pass.encode(u.getPassword()));
        repo.save(u);

    }

    public User findByEmail(String email) {
        return repo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Optional<User> findByIdUser(String id) {
        return repo.findById(id);
    }

    // update user password
    public boolean updateUserPassword(User user, String oldPassword, String newPassword) {
        if (!pass.matches(oldPassword, user.getPassword())) {
            return false;
        }

        user.setPassword(pass.encode(newPassword));
        user.setLastPasswordChange(LocalDateTime.now());

        repo.save(user);
        return true;
    }

}
