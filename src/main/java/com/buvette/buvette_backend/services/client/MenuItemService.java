package com.buvette.buvette_backend.services.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buvette.buvette_backend.model.client.MenuItem;
import com.buvette.buvette_backend.repository.client.MenuItemRepository;

@Service
public class MenuItemService {
    
    @Autowired
    private MenuItemRepository menuItemRepository;

    public MenuItem getItemById(String id){
        return menuItemRepository.findById(id).orElse(null);
    }
}
