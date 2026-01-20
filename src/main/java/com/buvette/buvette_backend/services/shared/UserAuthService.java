package com.buvette.buvette_backend.services.shared;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.buvette.buvette_backend.model.client.User;
import com.buvette.buvette_backend.repository.shared.UserRepository;
@Service
public class UserAuthService {
    @Autowired
    private UserRepository repo;
     @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    public UserAuthService(UserRepository repo) {
        this.repo = repo;
    }

    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repo.save(user);
    }

    public boolean authenticate(String email, String pass) {
    return repo.findByEmail(email)
            .map(user -> passwordEncoder.matches(pass, user.getPassword()))
            .orElse(false);
}

}
