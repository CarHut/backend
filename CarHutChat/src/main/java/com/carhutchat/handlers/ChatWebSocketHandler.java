package com.carhutchat.handlers;

import com.carhutchat.models.Message;
import com.carhutchat.services.ChatCommunicationService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private ChatCommunicationService chatCommunicationService;
    private Map<String, WebSocketSession> sessions = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String user = getUserFromSession(session);
        sessions.put(user, session);
        sendUndeliveredMessages(user);
    }

    private void sendUndeliveredMessages(String user) throws IOException {
        List<Message> undeliveredMessages = null;
        try {
            undeliveredMessages = chatCommunicationService.getUndeliveredMessages(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        for (Message message : undeliveredMessages) {
            WebSocketSession session = sessions.get(user);
            if (session != null && session.isOpen()) {
                session.sendMessage(new TextMessage(message.getMessage()));
            }
        }
        chatCommunicationService.markMessagesAsDelivered(undeliveredMessages);
    }

    private String getUserFromSession(WebSocketSession session) {
        return (String) session.getAttributes().get("username");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String user = getUserFromSession(session);
        String userId = chatCommunicationService.getUserIdByName(user);
        String recipient = parseRecipientFromTextMessage(message);
        String recipientId = chatCommunicationService.getUserIdByName(recipient);
        String content = parseMessageFromTextMessage(message);

        WebSocketSession recipientSession = sessions.get(recipient);
        if (recipientSession != null && recipientSession.isOpen()) {
            recipientSession.sendMessage(new TextMessage(parseMessageFromTextMessage(message)));
            chatCommunicationService.saveMessageToDatabase(userId, recipientId, content, true);
        } else {
            chatCommunicationService.saveMessageToDatabase(userId, recipientId, content, false);
        }
    }

    private String parseRecipientFromTextMessage(TextMessage message) {
        JSONObject jo = new JSONObject(message.getPayload());
        return jo.getString("recipient");
    }

    private String parseMessageFromTextMessage(TextMessage message) {
        JSONObject jo = new JSONObject(message.getPayload());
        return jo.getString("content");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String user = getUserFromSession(session); // Implement this method to extract the user info from session
        sessions.remove(user);
    }

}
