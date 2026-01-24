package com.buvette.buvette_backend.services.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buvette.buvette_backend.model.client.Notification;
import com.buvette.buvette_backend.model.client.User;
import com.buvette.buvette_backend.repository.shared.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {
    
    @Autowired
    private UserRepository userRepository;
    
    public void createNotification(String userId, Notification notification) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        notification.setCreatedAt(LocalDateTime.now());
        
        // Add to beginning of list for newest first
        user.getNotifications().add(0, notification);
        
        // Keep only last 50 notifications
        if (user.getNotifications().size() > 50) {
            List<Notification> trimmedList = user.getNotifications().subList(0, 50);
            user.setNotifications(trimmedList);
        }
        
        userRepository.save(user);
    }
    
    
    public List<Notification> getUserNotifications(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getNotifications();
    }
    
    public void markAsRead(String userId, String notificationId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        for (Notification notification : user.getNotifications()) {
            if (notification.getId().equals(notificationId)) {
                notification.setRead(true);
                break;
            }
        }
        
        userRepository.save(user);
    }
    
    public void markAllAsRead(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        for (Notification notification : user.getNotifications()) {
            notification.setRead(true);
        }
        
        userRepository.save(user);
    }
    
    public long getUnreadCount(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        long count = 0;
        for (Notification notification : user.getNotifications()) {
            if (!notification.isRead()) {
                count++;
            }
        }
        
        return count;
    }
}