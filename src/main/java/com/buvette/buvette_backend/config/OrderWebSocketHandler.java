package com.buvette.buvette_backend.config;


import com.buvette.buvette_backend.model.client.User;
import com.buvette.buvette_backend.model.shared.Order;

import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.TextMessage;
import org.springframework.stereotype.Component;

import java.util.concurrent.CopyOnWriteArraySet;
import java.util.Set;
import java.util.Map;

@Component
public class OrderWebSocketHandler extends TextWebSocketHandler {

    // Tous les sessions ouvertes
    private final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Récupérer l'utilisateur stocké par le JwtHandshakeInterceptor
        User user = (User) session.getAttributes().get("user");
        if (user != null) {
            System.out.println("✅ Worker connecté: " + user.getEmail());
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
    public void sendNewOrder(Order order) {
        TextMessage message;
        try {
            String json = "{ \"id\": " + order.getId() + ", \"status\": \"" + order.getStatus() + "\" }";
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

