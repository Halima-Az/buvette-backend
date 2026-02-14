package com.buvette.buvette_backend.services.worker;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.buvette.buvette_backend.enumAttribute.RequestOrderStatus;
import com.buvette.buvette_backend.enumAttribute.UserStatus;
import com.buvette.buvette_backend.model.client.User;
import com.buvette.buvette_backend.model.worker.RequestRegister;
import com.buvette.buvette_backend.repository.shared.UserRepository;
import com.buvette.buvette_backend.repository.worker.RequestWorkerRepository;
import com.buvette.buvette_backend.services.shared.EmailService;

@Service
public class RequestService {
    private RequestWorkerRepository repo;
    private EmailService emailService;
    private UserRepository userRepository;

    public RequestService(RequestWorkerRepository repo,
            EmailService emailService,
            UserRepository userRepository) {
        this.repo = repo;
        this.emailService = emailService;
        this.userRepository = userRepository;
    }

    public List<RequestRegister> getAllRequets() {
        return repo.findAll();
    }

    public void approveRequest(String id) {

        RequestRegister request = repo.findById(id).orElse(null);
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);
        request.setStatus(RequestOrderStatus.APPROVED);
        repo.save(request);
        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
        String link = "http://localhost:5173/verify?token=" + token;
        emailService.acceptWorkerRequest(request.getFirstName(), request.getEmail(), link);

    }

    public void rejectRequest(String id) {

        RequestRegister request = repo.findById(id).orElse(null);
        request.setStatus(RequestOrderStatus.REJECTED);
        repo.save(request);

    }

    public User getUserDetails(String requestId) {
        RequestRegister requestRegister = repo.findById(requestId).orElse(null);
        User user = userRepository.findByEmail(requestRegister.getEmail()).orElse(null);
        return user;

    }

    // update user status
    public void updateUserStatus(String email, String status) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null)
            return;

        if ("deactivate".equals(status)) {
            user.setStatus(UserStatus.DISABLED);
        } else if ("activate".equals(status)) {
            user.setStatus(UserStatus.ACTIVE);
        }

        userRepository.save(user);
    }

}