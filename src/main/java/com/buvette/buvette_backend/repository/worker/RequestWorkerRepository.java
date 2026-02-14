package com.buvette.buvette_backend.repository.worker;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.buvette.buvette_backend.model.worker.RequestRegister;

@Repository
public interface RequestWorkerRepository extends MongoRepository<RequestRegister, String> {
    
}
