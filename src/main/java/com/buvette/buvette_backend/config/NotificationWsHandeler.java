package com.buvette.buvette_backend.config;

import com.buvette.buvette_backend.model.client.Notification;
import com.buvette.buvette_backend.model.client.User;

import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.TextMessage;
import org.springframework.stereotype.Component;

import java.util.concurrent.CopyOnWriteArraySet;
import java.util.Set;

@Component
public class NotificationWsHandeler extends TextWebSocketHandler {

    // Tous les sessions ouvertes
    private final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Récupérer l'utilisateur stocké par le JwtHandshakeInterceptor
        User user = (User) session.getAttributes().get("user");
        if (user != null) {
            System.out.println("✅ Client connecté: " + user.getEmail());
            sessions.add(session);
        } else {
            // Fermer la session si pas d'utilisateur
            session.close();
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        sessions.remove(session);
    }

    // Méthode pour envoyer une commande à tous les workers connectés
    public void sendNewOrder(Notification notification) {
        TextMessage message;
        try {
            String json =
                "{"
            + "\"id\":\"" + notification.getId() + "\","
            + "\"userId\":\"" + notification.getUserId() + "\","
            + "\"title\":\"" + notification.getTitle() + "\","
            + "\"message\":\"" + notification.getMessage() + "\","
            + "\"type\":\"" + notification.getType() + "\","
            + "\"orderId\":\"" + notification.getOrderId() + "\","
            + "\"read\":" + notification.isRead() + ","
            + "\"createdAt\":\"" + notification.getCreatedAt() + "\""
            + "}";

            message = new TextMessage(json);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        for (WebSocketSession session : sessions) {
            try {
                if (session.isOpen()) {
                    session.sendMessage(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
}
