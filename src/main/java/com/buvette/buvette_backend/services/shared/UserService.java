package com.buvette.buvette_backend.services.shared;

import org.springframework.beans.factory.annotation.Autowired;
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

    public void engister(User u){
        u.setPassword(pass.encode(u.getPassword()));
         repo.save(u);

    }
    public User findByEmail(String email){
        return repo.findByEmail(email)   .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
