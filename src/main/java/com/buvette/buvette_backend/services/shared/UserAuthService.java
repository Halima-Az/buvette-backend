package com.buvette.buvette_backend.services.shared;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.buvette.buvette_backend.enumAttribute.RequestOrderStatus;
import com.buvette.buvette_backend.enumAttribute.UserStatus;
import com.buvette.buvette_backend.model.client.User;
import com.buvette.buvette_backend.model.worker.RequestRegister;
import com.buvette.buvette_backend.repository.shared.UserRepository;
import com.buvette.buvette_backend.repository.worker.RequestWorkerRepository;

@Service
public class UserAuthService {
    @Autowired
    private UserRepository repo;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;
    @Autowired
    private RequestWorkerRepository requestWorker;

    public UserAuthService(UserRepository repo) {
        this.repo = repo;
    }

    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repo.save(user);
    }

    public void sendVerificationEmail(User user) {
        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repo.save(user);
        String link = "http://localhost:5173/verify?token=" + token;
        emailService.sendVerifiedPasswordEmail(user.getEmail(), link);

    }
    public void sendRequestToAdmin(User user) {
        if(!repo.existsByUsername(user.getUsername() )&& !repo.existsByEmail(user.getEmail()  )){   
        RequestRegister request= new RequestRegister(user.getFname(),
                                                     user.getLname(),
                                                     user.getEmail(),
                                                    RequestOrderStatus.PENDING,
                                                    LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(user.getPassword())); 
        user.setStatus(UserStatus.DISABLED);                                           
        requestWorker.save(request);
        repo.save(user);

        }

    }

    public boolean verifyUserEmail(String token) {
        User user = repo.findByVerificationToken(token);

        if (user == null) {
            return false;
        }

        user.setEmailVerified(true);
        user.setVerificationToken(null);
        repo.save(user);
        return true;

    }

    public String authenticate(String email, String pass) {

        User user = repo.findByEmail(email).orElse(null);

        if (user == null) {
            return "EMAIL_NOT_FOUND";
        }

        if (!user.isEmailVerified()) {
            return "EMAIL_NOT_VERIFIED";
        }

        if (!passwordEncoder.matches(pass, user.getPassword())) {
            return "BAD_PASSWORD";
        }
        if(user.getStatus()== UserStatus.DISABLED){
            return "DISABLED_ACCOUNT";
        }

        return "SUCCESS";
    }

    public String getRoleByEmail(String email) {
        return repo.findByEmail(email)
                .map(User::getRole)
                .orElse(null);
    }

    public boolean forgotPassword(String identifier, User user){

         if (user == null) {
            return false;
        }

        String token = UUID.randomUUID().toString();

        user.setResetPasswordToken(token);
        user.setResetPasswordExpiry(LocalDateTime.now().plusMinutes(10));

        this.save(user);

        String link = "http://localhost:5173/reset-password?token=" + token;

        emailService.sendResetPasswordEmail(identifier, link);
        return true;
    }

}
