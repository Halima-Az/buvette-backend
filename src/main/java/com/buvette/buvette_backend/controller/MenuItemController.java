package com.buvette.buvette_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

import com.buvette.buvette_backend.model.MenuItem;
import com.buvette.buvette_backend.repository.MenuItemRepository;
import com.buvette.buvette_backend.enumAttribute.ItemCategory;

import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class MenuItemController {

    @Autowired
    private MenuItemRepository menuItemRepository;

    @GetMapping("/api/menu")
    public List<MenuItem> getMenu() {
        return menuItemRepository.findAll();
    }

    @GetMapping("/api/categories")
    public List<String> getCategories() {
        return Arrays.stream(ItemCategory.values())
                    .map(Enum::name)
                    .toList();
    }

    @GetMapping("/api/menu-items")
    public List<MenuItem> getMenuItems(@RequestParam(required = false) String category){
        if (category == null) {
            return menuItemRepository.findAll();
        }
        try {
            ItemCategory catEnum = ItemCategory.valueOf(category.toUpperCase());
            return menuItemRepository.findByItemCategory(catEnum);
        } catch (IllegalArgumentException e) {
            return List.of(); // invalid category
        }
    }

}

