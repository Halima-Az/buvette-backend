package com.buvette.buvette_backend.repository.shared;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.buvette.buvette_backend.model.client.User;
public interface UserRepository extends MongoRepository<User,String>{
    
    Optional<User> findByEmail(String email);
}
