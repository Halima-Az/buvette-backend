package com.buvette.buvette_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import com.buvette.buvette_backend.model.MenuItem;
import com.buvette.buvette_backend.repositories.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class MenuItemController {

    @Autowired
    private MenuItemRepository menuItemRepository;

    @GetMapping("/api/menu")
    public List<MenuItem> getMenu() {
        return menuItemRepository.findAll();
    }
}

