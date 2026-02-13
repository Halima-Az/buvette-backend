package com.buvette.buvette_backend.repository.shared;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.buvette.buvette_backend.model.client.User;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);

    Optional<User> findById(String id);

    String findRoleByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findByResetPasswordToken(String token);

    boolean existsByUsername(String username);
    User findByVerificationToken(String token);
}
