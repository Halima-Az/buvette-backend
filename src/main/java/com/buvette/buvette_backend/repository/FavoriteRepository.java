package com.buvette.buvette_backend.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.buvette.buvette_backend.model.Favorite;

public interface FavoriteRepository extends MongoRepository<Favorite,String>{
        void deleteByIdUserAndItemId(String idUser, String itemId);
        List<Favorite>findByIdUser(String email);
}
