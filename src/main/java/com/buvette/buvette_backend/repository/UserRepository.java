package com.buvette.buvette_backend.repository;

import com.buvette.buvette_backend.model.User;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
public interface UserRepository extends MongoRepository<User,String>{
    
    Optional<User> findByEmail(String email);
}
