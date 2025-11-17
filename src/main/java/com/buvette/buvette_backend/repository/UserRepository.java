package com.buvette.buvette_backend.repository;

import com.buvette.buvette_backend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
public interface UserRepository extends MongoRepository<User,String>{
    public User findByEmail(String email);
}
