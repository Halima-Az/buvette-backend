package com.buvette.buvette_backend.services.shared;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
     public User getUserByEmail(String email) {
        return repo.findByEmail(email).orElse(null);
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

    public void save(User user){
        repo.save(user);
    }

    public void resetPassword(String token,String newPassword){
        User user = repo.findByResetPasswordToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "address email not found"));

        if (user.getResetPasswordExpiry().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Token expired");
        }

        user.setPassword(pass.encode(newPassword));
        user.setLastPasswordChange(LocalDateTime.now());

        user.setResetPasswordToken(null);
        user.setResetPasswordExpiry(null);

        repo.save(user);

    }

}
